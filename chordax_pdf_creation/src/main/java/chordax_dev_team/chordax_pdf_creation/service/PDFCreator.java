package chordax_dev_team.chordax_pdf_creation.service;

import chordax_dev_team.chordax_pdf_creation.model.Line;
import chordax_dev_team.chordax_pdf_creation.model.Song;
import chordax_dev_team.chordax_pdf_creation.model.Tone;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PDFCreator {

	private final Song song;
	private final String filePath;
	private static final float UNIT_CONVERTER = 2.834645669f; // 1mm ≈ 2.8346pt

	public PDFCreator(Song song) {
		this.song = song;
		this.filePath = "src/main/resources/pdfs/" + sanitizeFileName(song.title()) + ".pdf";
	}

	public File createPdf() throws IOException, DocumentException {
		File file = new File(filePath);

		if (file.exists()) {
			return file;
		}

		final int PAGE_WIDTH_MM = 210;
		final int PAGE_HEIGHT_MM = 297;
		final int MARGIN_LEFT_MM = 25;
		final int MARGIN_TOP_MM = 25;
		final int MARGIN_BOTTOM_MM = 25;
		final int LINE_HEIGHT_PT = 15;
		final int CHORDS_LINE_HEIGHT_PT = 6;

		Rectangle pageSize = new Rectangle(PAGE_WIDTH_MM * UNIT_CONVERTER, PAGE_HEIGHT_MM * UNIT_CONVERTER);
		Document document = new Document(pageSize);
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
		document.open();

		LineSeparator separator = new LineSeparator(1.0f, 110.0f, BaseColor.BLACK, Element.ALIGN_CENTER, -17.0f * UNIT_CONVERTER);
		document.add(new Chunk(separator));

		PdfContentByte cb = writer.getDirectContent();
		cb.saveState();

		// Load fonts
		BaseFont titleFont = loadFont("src/main/resources/static/fonts/Roboto-Black.ttf");
		BaseFont bodyFont = loadFont("src/main/resources/static/fonts/Roboto-Bold.ttf");

		// Title
		cb.beginText();
		cb.setFontAndSize(titleFont, 18);
		cb.moveText(MARGIN_LEFT_MM * UNIT_CONVERTER, (PAGE_HEIGHT_MM - MARGIN_TOP_MM) * UNIT_CONVERTER);
		cb.showText(song.title());
		cb.endText();

		// Metadata
		cb.beginText();
		cb.setFontAndSize(titleFont, 10);
		cb.moveText(MARGIN_LEFT_MM * UNIT_CONVERTER, (PAGE_HEIGHT_MM - MARGIN_TOP_MM - 10) * UNIT_CONVERTER);
		cb.showText("Composer: " + song.composer() + " | Author: " + song.author());
		cb.endText();

		// Body
		cb.setFontAndSize(titleFont, 12);
		int verseY = 260;
		for (Line line : song.lines()) {
			for (Tone tone : line.tones()) {
				cb.beginText();
				cb.moveText((MARGIN_LEFT_MM + tone.position()) * UNIT_CONVERTER, verseY * UNIT_CONVERTER);
				cb.showText(tone.chord());
				cb.endText();
			}
			cb.beginText();
			cb.moveText(MARGIN_LEFT_MM * UNIT_CONVERTER, (verseY - CHORDS_LINE_HEIGHT_PT) * UNIT_CONVERTER);
			cb.showText(line.lyrics());
			cb.endText();
			verseY -= LINE_HEIGHT_PT;
		}

		// Signature
		cb.beginText();
		cb.setFontAndSize(bodyFont, 13);
		cb.moveText((PAGE_WIDTH_MM - MARGIN_LEFT_MM - titleFont.getWidthPoint("Chordax", 13)) * UNIT_CONVERTER,
				MARGIN_BOTTOM_MM * UNIT_CONVERTER);
		cb.showText("Chordax");
		cb.endText();

		try {
			Image logo = Image.getInstance("src/main/resources/static/img/chordax.png");

			// Resize if needed
			logo.scaleAbsolute(200, 75); // width, height in points

			// Position at bottom-left corner
			logo.setAbsolutePosition(25 * UNIT_CONVERTER, 10 * UNIT_CONVERTER); // x, y in points

			cb.addImage(logo);
		} catch (Exception e) {
			System.err.println("Failed to load footer image: " + e.getMessage());
		}

		cb.restoreState();
		document.close();

		return file;
	}

	private static String sanitizeFileName(String title) {
		return title.replaceAll("[^a-zA-Z0-9\\-_\\. ]", "_");
	}

	private static BaseFont loadFont(String path) throws IOException {
		try {
			return BaseFont.createFont(path, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		} catch (Exception e) {
			throw new IOException("Failed to load font: " + path, e);
		}
	}
}
