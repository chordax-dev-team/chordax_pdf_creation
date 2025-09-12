package chordax_dev_team.chordax_pdf_creation.controller;

import chordax_dev_team.chordax_pdf_creation.dto.PDFDto;
import chordax_dev_team.chordax_pdf_creation.service.PDFService;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/pdfs")
@CrossOrigin(origins = "*") // Consider restricting this in production
public class PDFCreationController {

	private final PDFService pdfService;

	@Autowired
	public PDFCreationController(PDFService pdfService) {
		this.pdfService = pdfService;
	}

	@GetMapping("/{songId}")
	public ResponseEntity<PDFDto> getPDF(@PathVariable Long songId) {
		try {

			PDFDto pdfDto = pdfService.getPDF(songId);
			return ResponseEntity.ok(pdfDto);

		} catch (IOException | DocumentException | InterruptedException ex) {
			// Ideally log the exception here
			return ResponseEntity.status(HttpStatus.SEE_OTHER).build();
		}
	}
}
