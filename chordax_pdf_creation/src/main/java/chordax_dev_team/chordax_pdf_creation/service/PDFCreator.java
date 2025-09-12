package chordax_dev_team.chordax_pdf_creation.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import chordax_dev_team.chordax_pdf_creation.model.Line;
import chordax_dev_team.chordax_pdf_creation.model.Song;
import chordax_dev_team.chordax_pdf_creation.model.Tone;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

public class PDFCreator {

	private final Song song;
	private final String FILE_PATH;
	private static final float UNIT_CONVERTER = 2.834645669f; // 1mm = ~2.8346 points

	public PDFCreator(Song song) {
		this.song = song;
		this.FILE_PATH = "src/main/resources/pdfs/" + song.getTitle().toLowerCase() + ".pdf";
	}

	public File createPdf() throws IOException, DocumentException {
		File file = new File(this.FILE_PATH);

		if (!file.exists()) {
			final int PAGE_WIDTH = 210;
			final int PAGE_HEIGHT = 297;
			final int MARGIN_LEFT = 25;
			final int MARGIN_TOP = 25;
			final int MARGIN_BOTTOM = 25;
			final int LINE_HEIGHT = 15;
			final int CHORDS_LINE_HEIGHT = 6;

			Rectangle pageSize = new Rectangle(PAGE_WIDTH * UNIT_CONVERTER, PAGE_HEIGHT * UNIT_CONVERTER);
			Document document = new Document(pageSize);
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
			document.open();

			LineSeparator separator = new LineSeparator(1.0f, 110.0f, BaseColor.BLACK, 100, -17.0f * UNIT_CONVERTER);
			document.add(new Chunk(separator));

			PdfContentByte cb = writer.getDirectContent();
			cb.saveState();

			// Load fonts
			BaseFont titleFont = loadFont("src/main/resources/static/fonts/Roboto-Black.ttf");
			BaseFont bodyFont = loadFont("src/main/resources/static/fonts/Roboto-Bold.ttf");

			// Title
			cb.beginText();
			cb.setFontAndSize(titleFont, 18);
			cb.moveText(MARGIN_LEFT * UNIT_CONVERTER, (PAGE_HEIGHT - MARGIN_TOP) * UNIT_CONVERTER);
			cb.showText(song.getTitle());
			cb.endText();

			// Optional metadata
			cb.beginText();
			cb.setFontAndSize(titleFont, 10);
			cb.moveText(MARGIN_LEFT * UNIT_CONVERTER, (PAGE_HEIGHT - MARGIN_TOP - 10) * UNIT_CONVERTER);
			cb.showText("Composer: " + song.getComposer() + " | Author: " + song.getAuthor());
			cb.endText();

			// Body
			cb.setFontAndSize(titleFont, 12);
			int versePositionY = 260;
			for (Line line : song.getLines()) {
				for (Tone tone : line.getTones()) {
					cb.beginText();
					cb.moveText((MARGIN_LEFT + tone.getPosition()) * UNIT_CONVERTER, versePositionY * UNIT_CONVERTER);
					cb.showText(tone.getChord());
					cb.endText();
				}
				cb.beginText();
				cb.moveText(MARGIN_LEFT * UNIT_CONVERTER, (versePositionY - CHORDS_LINE_HEIGHT) * UNIT_CONVERTER);
				cb.showText(line.getLyrics());
				cb.endText();
				versePositionY -= LINE_HEIGHT;
			}

			// Signature
			cb.beginText();
			cb.setFontAndSize(bodyFont, 13);
			cb.moveText((PAGE_WIDTH - MARGIN_LEFT - titleFont.getWidthPoint("Chordax", 13)) * UNIT_CONVERTER,
					MARGIN_BOTTOM * UNIT_CONVERTER);
			cb.showText("Chordax");
			cb.endText();

			cb.restoreState();
			document.close();
		}

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
