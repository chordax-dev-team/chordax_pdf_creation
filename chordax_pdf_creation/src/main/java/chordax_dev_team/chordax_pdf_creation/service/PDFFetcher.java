package chordax_dev_team.chordax_pdf_creation.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.itextpdf.text.DocumentException;

import chordax_dev_team.chordax_pdf_creation.model.Song;

public class PDFFetcher {
	
	public final static byte[] getPDF(Song song) throws IOException, DocumentException, InterruptedException{
		
		PDFCreator pdfCreated = new PDFCreator(song);
		
		File file = pdfCreated.createPdf();

		String fileSource = file.getPath();

		Path pdfPath = Paths.get(fileSource);
		byte[] pdfData = Files.readAllBytes(pdfPath);
			
		file.delete();
		
		return pdfData;
		
	}
}
