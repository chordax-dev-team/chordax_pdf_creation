package chordax_dev_team.chordax_pdf_creation.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.itextpdf.text.DocumentException;

public class PDFFetcher {
	
	public final static byte[] getPDF(int songId) throws IOException, DocumentException, InterruptedException{
		
		JsonParser jPar = new JsonParser(songId + ".txt");
		
		PDFCreator pdfCreated = new PDFCreator(jPar.convertToJson());
		
		File file = pdfCreated.createPdf();

		String fileSource = file.getPath();

		Path pdfPath = Paths.get(fileSource);
		byte[] pdfData = Files.readAllBytes(pdfPath);
			
//		file.delete();
		
		return pdfData;
		
	}
}
