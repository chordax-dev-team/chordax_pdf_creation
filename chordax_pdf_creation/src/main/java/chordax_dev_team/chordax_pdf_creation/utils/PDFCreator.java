package chordax_dev_team.chordax_pdf_creation.utils;

import chordax_dev_team.chordax_pdf_creation.model.Line;
import chordax_dev_team.chordax_pdf_creation.model.Song;
import chordax_dev_team.chordax_pdf_creation.model.Tone;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
public class PDFCreator {

	private static final Logger logger = LoggerFactory.getLogger(PDFCreator.class);
	private static final float UNIT_CONVERTER = 2.834645669f; // 1mm ≈ 2.8346pt

	public byte[] createPdf(Song song) throws IOException, DocumentException {
		logger.info("Starting PDF creation for song '{}'", song.title());

		final int PAGE_WIDTH_MM = 210;
		final int PAGE_HEIGHT_MM = 297;
		final int MARGIN_LEFT_MM = 25;
		final int MARGIN_TOP_MM = 25;
		final int MARGIN_BOTTOM_MM = 25;
		final int LINE_HEIGHT_PT = 15;
		final int CHORDS_LINE_HEIGHT_PT = 6;

		Rectangle pageSize = new Rectangle(PAGE_WIDTH_MM * UNIT_CONVERTER, PAGE_HEIGHT_MM * UNIT_CONVERTER);
		Document document = new Document(pageSize);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PdfWriter writer = PdfWriter.getInstance(document, outputStream);
		document.open();

		LineSeparator separator = new LineSeparator(1.0f, 110.0f, BaseColor.BLACK, Element.ALIGN_CENTER, -17.0f * UNIT_CONVERTER);
		document.add(new Chunk(separator));

		PdfContentByte cb = writer.getDirectContent();
		cb.saveState();

		// Load fonts
		BaseFont titleFont = loadFont("src/main/resources/static/fonts/Roboto-Black.ttf");
		BaseFont bodyFont = loadFont("src/main/resources/static/fonts/Roboto-Bold.ttf");

		logger.debug("Fonts loaded successfully");

		// Title
		cb.beginText();
		cb.setFontAndSize(titleFont, 18);
		cb.moveText(MARGIN_LEFT_MM * UNIT_CONVERTER, (PAGE_HEIGHT_MM - MARGIN_TOP_MM) * UNIT_CONVERTER);
		cb.showText(song.title());
		cb.endText();

		// Metadata - Composer
		cb.beginText();
		cb.setFontAndSize(titleFont, 10);
		cb.moveText(MARGIN_LEFT_MM * UNIT_CONVERTER, (PAGE_HEIGHT_MM - MARGIN_TOP_MM - 10) * UNIT_CONVERTER);
		cb.showText("Composer: " + song.composer());
		cb.endText();

		// Metadata - Author (shifted down by 12pt)
		cb.beginText();
		cb.setFontAndSize(titleFont, 10);
		cb.moveText(MARGIN_LEFT_MM * UNIT_CONVERTER, (PAGE_HEIGHT_MM - MARGIN_TOP_MM - 22) * UNIT_CONVERTER);
		cb.showText("Author: " + song.author());
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
			logo.scaleAbsolute(123, 39);
			logo.setAbsolutePosition(25 * UNIT_CONVERTER, 10 * UNIT_CONVERTER);
			cb.addImage(logo);
			logger.debug("Logo image added to PDF");
		} catch (Exception e) {
			logger.warn("Failed to load footer image: {}", e.getMessage());
		}

		cb.restoreState();
		document.close();

		logger.info("PDF creation completed for song '{}'", song.title());
		return outputStream.toByteArray();
	}

	private static BaseFont loadFont(String path) throws IOException {
		try {
			return BaseFont.createFont(path, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		} catch (Exception e) {
			logger.error("Failed to load font from path '{}'", path, e);
			throw new IOException("Failed to load font: " + path, e);
		}
	}
}