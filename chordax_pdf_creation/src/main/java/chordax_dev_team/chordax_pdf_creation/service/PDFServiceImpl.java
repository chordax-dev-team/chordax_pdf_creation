package chordax_dev_team.chordax_pdf_creation.service;

import java.io.IOException;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.itextpdf.text.DocumentException;

//Annotation
@Service
//Class
//Implementing PDFService interface
public class PDFServiceImpl implements PDFService{

	public ResponseEntity<byte[]> getResponseWithPDF(long songId) throws IOException, DocumentException, InterruptedException{
		
		JsonParser jPar = new JsonParser(songId + ".txt");
		
	    byte[] contents = PDFFetcher.getPDF(jPar.convertToJson());

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_PDF);
	    
	    // Here you have to set the actual filename of your pdf
	    String filename = jPar.getFileName();
	    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
	    ContentDisposition cd = ContentDisposition.parse(filename);
	    headers.setContentDisposition(cd);
	    ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
		return response;
		
	}
}
