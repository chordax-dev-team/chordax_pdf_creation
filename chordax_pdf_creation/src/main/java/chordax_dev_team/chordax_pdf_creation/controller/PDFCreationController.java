package chordax_dev_team.chordax_pdf_creation.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;

import chordax_dev_team.chordax_pdf_creation.service.PDFService;

@RestController
@RequestMapping("api/v1/pdfs")
public class PDFCreationController {
	
	@Autowired
	private PDFService pdfService;
	
	@GetMapping("/{songId}")
	@CrossOrigin(origins="*")
	public ResponseEntity<byte[]> getPDF( @PathVariable Integer songId)
			throws IOException, DocumentException, InterruptedException {	
		    
		return pdfService.getResponseWithPDF(songId);
	}
}