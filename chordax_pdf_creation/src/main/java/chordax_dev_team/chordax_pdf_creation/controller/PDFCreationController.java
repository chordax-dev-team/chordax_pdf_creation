package chordax_dev_team.chordax_pdf_creation.controller;

import java.io.IOException;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;

import chordax_dev_team.chordax_pdf_creation.service.PDFFetcher;

@RestController
public class PDFCreationController {
	
	@GetMapping("/song/{songId}")
	@CrossOrigin(origins="*")
	public ResponseEntity<byte[]> display( @PathVariable Integer songId)
			throws IOException, DocumentException, InterruptedException {
		
		    byte[] contents = PDFFetcher.getPDF(songId);

		    HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_PDF);
		    
		    // Here you have to set the actual filename of your pdf
		    String filename = "Pharmacode_.pdf";
//		    headers.set("content-disposition", filename);
		    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		    ContentDisposition cd = ContentDisposition.parse(filename);
		    headers.setContentDisposition(cd);
		    ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
			System.out.println(contents.length);
		    return response;

	}

}
