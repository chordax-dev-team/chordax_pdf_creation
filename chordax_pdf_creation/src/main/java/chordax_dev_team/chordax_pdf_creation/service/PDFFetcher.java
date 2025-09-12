package chordax_dev_team.chordax_pdf_creation.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.itextpdf.text.DocumentException;
import chordax_dev_team.chordax_pdf_creation.model.Song;

public class PDFFetcher {

	public static byte[] getPDF(Song song) throws IOException, DocumentException {
		PDFCreator pdfCreator = new PDFCreator(song);
		File pdfFile = pdfCreator.createPdf();

		Path pdfPath = pdfFile.toPath();
		byte[] pdfData = Files.readAllBytes(pdfPath);

		boolean deleted = pdfFile.delete();
		if (!deleted) {
			System.err.println("Warning: Temporary PDF file could not be deleted: " + pdfFile.getPath());
		}

		return pdfData;
	}
}