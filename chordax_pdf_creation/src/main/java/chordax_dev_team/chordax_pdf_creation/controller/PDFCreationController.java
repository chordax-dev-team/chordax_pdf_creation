package chordax_dev_team.chordax_pdf_creation.controller;

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
	public ResponseEntity<byte[]> getPDF(@PathVariable("songId") Long songId) {
		try {
			byte[] pdfData = pdfService.getPDF(songId);

			if (pdfData == null || pdfData.length == 0) {
				return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
			}

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_PDF);
			headers.setContentDisposition(ContentDisposition.builder("inline")
					.filename("song_" + songId + ".pdf")
					.build());

			return new ResponseEntity<>(pdfData, headers, HttpStatus.OK);

		} catch (IOException | DocumentException | InterruptedException e) {
			// Log the error if you have a logger
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
