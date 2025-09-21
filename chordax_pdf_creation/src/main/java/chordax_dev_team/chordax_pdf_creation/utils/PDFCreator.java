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

	public byte[] createPdf(Song song) throws IOException, DocumentException {
		logger.info("Starting PDF creation for song '{}'", song.title());

		final float UNIT_CONVERTER = 2.834645669f; // 1mm ≈ 2.8346pt

		final int PAGE_WIDTH_MM = 210;
		final int PAGE_HEIGHT_MM = 297;
		final int MARGIN_LEFT_MM = 25;
		final int MARGIN_TOP_MM = 25;
		final int MARGIN_BOTTOM_MM = 25;
		final int LINE_HEIGHT_PT = 15;
		final int CHORDS_LINE_HEIGHT_PT = 5;

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

		final float CARET_X = MARGIN_LEFT_MM * UNIT_CONVERTER;
		float caretY = (PAGE_HEIGHT_MM - MARGIN_TOP_MM) * UNIT_CONVERTER;
		// Title
		cb.setRGBColorFill(190, 15, 15);
		cb.beginText();
		cb.setFontAndSize(titleFont, 24);

		cb.moveText(CARET_X, caretY);
		cb.showText(song.title());
		cb.endText();

		// Metadata - Composer
		caretY -= 15 * UNIT_CONVERTER;
		cb.setColorFill(BaseColor.BLACK);
		cb.beginText();
		cb.moveText(CARET_X, caretY);
		cb.setFontAndSize(titleFont, 8);
		cb.showText("COMPOSER");
		cb.endText();
		cb.beginText();
		cb.moveText(CARET_X + 50, caretY);
		cb.showText("|");
		cb.endText();
		cb.beginText();
		cb.moveText(CARET_X + 60, caretY);
		cb.setFontAndSize(titleFont, 10);
		cb.showText(song.composer());
		cb.endText();

		// Metadata - Author (shifted down by 12pt)
		caretY -= 5 * UNIT_CONVERTER;
		cb.beginText();
		cb.moveText(CARET_X, caretY);
		cb.setFontAndSize(titleFont, 8);
		cb.showText("AUTHOR");
		cb.endText();
		cb.beginText();
		cb.moveText(CARET_X + 50, caretY);
		cb.showText("|");
		cb.endText();
		cb.beginText();
		cb.moveText(CARET_X + 60, caretY);
		cb.setFontAndSize(titleFont, 10);
		cb.showText(song.author());
		cb.endText();

		// Body
		cb.setFontAndSize(titleFont, 12);
		for (Line line : song.lines()) {
			caretY -= LINE_HEIGHT_PT * UNIT_CONVERTER;
			for (Tone tone : line.tones()) {
				cb.beginText();
				cb.moveText(CARET_X + tone.position(), caretY);
				cb.showText(tone.chord());
				cb.endText();
			}
			cb.beginText();
			cb.moveText(CARET_X, caretY - CHORDS_LINE_HEIGHT_PT * UNIT_CONVERTER);
			cb.showText(line.lyrics());
			cb.endText();
		}

		float llx = CARET_X;
		final float LLY = MARGIN_BOTTOM_MM * UNIT_CONVERTER - 20;
		float urx = llx + 6;
		final float URY = LLY + 6;

		Rectangle rect = new Rectangle(llx, LLY, urx, URY);
		rect.setBorder(Rectangle.BOX);
		rect.setBorderWidth(0.25f);
		rect.setBorderColor(BaseColor.BLACK);
		rect.setBackgroundColor(BaseColor.BLACK);
		document.add(rect);


		final int IMAGE_WIDTH = 90;
		final int IMAGE_HEIGHT = 30;
		final float IMAGE_POSITION_X = (PAGE_WIDTH_MM - MARGIN_LEFT_MM) * UNIT_CONVERTER - IMAGE_WIDTH;
		final float IMAGE_POSITION_Y = (MARGIN_BOTTOM_MM * UNIT_CONVERTER) - IMAGE_HEIGHT;
		try {
			Image logo = Image.getInstance("src/main/resources/static/img/chordax.png");
			logo.scaleAbsolute(IMAGE_WIDTH, IMAGE_HEIGHT);
			logo.setAbsolutePosition(IMAGE_POSITION_X, IMAGE_POSITION_Y);
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