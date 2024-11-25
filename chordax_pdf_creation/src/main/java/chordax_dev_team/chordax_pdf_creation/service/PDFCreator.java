package chordax_dev_team.chordax_pdf_creation.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import chordax_dev_team.chordax_pdf_creation.model.Song;
import chordax_dev_team.chordax_pdf_creation.model.Tone;
import chordax_dev_team.chordax_pdf_creation.model.Verse;

public class PDFCreator {

	private final Song song;
	private final String FILE_PATH;

	private final static float UNIT_CONVERTER = 2.8346456692913385826771653543307f; // 1mm / (1/72) inch

	public PDFCreator(Song song) {
		this.song = song;
		this.FILE_PATH = "src/main/resources/pdfs/" + song.getTitle() + ".pdf";
	}

	public File createPdf() throws IOException, DocumentException, InterruptedException {

		File file = new File(this.FILE_PATH);

		if (!file.exists()) {

			Document document = new Document();
			final int PAGE_WIDTH = 210;
			final int PAGE_HEIGHT = 297;
			Rectangle pageSize = new Rectangle(PAGE_WIDTH * UNIT_CONVERTER, PAGE_HEIGHT * UNIT_CONVERTER);
			document.setPageSize(pageSize);

			PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(this.FILE_PATH));

			document.open();

			// line separating header from body
			LineSeparator ls = new LineSeparator(1.0f, 110.0f, BaseColor.BLACK, 100, -17.0f * UNIT_CONVERTER);
			document.add(new Chunk(ls));

			PdfContentByte cb = pdfWriter.getDirectContent();
			cb.saveState();

			// setting header of PDF
			final int MARGIN_LEFT = 25;
			final int MARGIN_TOP = 25;
			final int MARGIN_BOTTOM = 25;
			int fontSize = 18;
			
			// 'IDENTITY_H' as type of fonts is to support glyphs (in different languages)
			BaseFont bf = BaseFont.createFont("src/main/resources/static/fonts/Roboto-Black.ttf", BaseFont.IDENTITY_H,
					BaseFont.EMBEDDED);
			cb.beginText();
			cb.setFontAndSize(bf, fontSize);
			cb.moveText(MARGIN_LEFT * UNIT_CONVERTER, (PAGE_HEIGHT - MARGIN_TOP) * UNIT_CONVERTER);
			cb.showText(song.getTitle());
			cb.endText();

			// setting body text
			fontSize = 12;
			cb.setFontAndSize(bf, fontSize);
			int versePositionY = 260;
			int LINE_HEIGHT = 15;
			int CHORDS_LINE_HEIGHT = 6;
			for (Verse v : song.getVerses()) {
				for (Tone t : v.getTones()) {
					cb.beginText();
					cb.moveText((MARGIN_LEFT + t.getPosition() * UNIT_CONVERTER), versePositionY * UNIT_CONVERTER);
					cb.showText(t.getTone());
					cb.endText();
				}
				cb.beginText();
				cb.moveText(MARGIN_LEFT * UNIT_CONVERTER, (versePositionY - CHORDS_LINE_HEIGHT) * UNIT_CONVERTER);
				cb.showText(v.getLyrics());
				cb.endText();

				versePositionY -= LINE_HEIGHT;
				
			}

			// signature "chordax"
			fontSize = 13;
			BaseFont bfb = BaseFont.createFont("src/main/resources/static/fonts/Roboto-Bold.ttf", BaseFont.IDENTITY_H,
					BaseFont.EMBEDDED);
			cb.beginText();
			cb.setFontAndSize(bfb, fontSize);
			cb.moveText((PAGE_WIDTH - MARGIN_LEFT - bf.getWidthPoint("Chordax", fontSize)) * UNIT_CONVERTER,
					MARGIN_BOTTOM * UNIT_CONVERTER);
			cb.showText("Chordax");
			cb.endText();

			cb.restoreState();

			document.close();
		}
		return file;
	}
}
