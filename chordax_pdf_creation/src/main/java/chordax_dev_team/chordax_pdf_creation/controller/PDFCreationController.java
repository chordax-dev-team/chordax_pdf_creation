package chordax_dev_team.chordax_pdf_creation.controller;

import chordax_dev_team.chordax_pdf_creation.dto.PDFDto;
import chordax_dev_team.chordax_pdf_creation.model.Song;
import chordax_dev_team.chordax_pdf_creation.service.PDFService;
import chordax_dev_team.chordax_pdf_creation.service.SongService;
import com.itextpdf.text.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/pdfs")
@CrossOrigin(origins = "*") // Consider restricting this in production
public class PDFCreationController {

	private static final Logger logger = LoggerFactory.getLogger(PDFCreationController.class);

	private final PDFService pdfService;
	private final SongService songService;

	public PDFCreationController(PDFService pdfService, SongService songService) {
		this.pdfService = pdfService;
		this.songService = songService;
	}

	@GetMapping("{userId}/{songId}")
	public ResponseEntity<PDFDto> getPDF(@PathVariable Long userId, @PathVariable Long songId) throws DocumentException, IOException {
		logger.info("Received request to generate PDF for userId={}, songId={}", userId, songId);

		Song song = songService.fetchSong(userId, songId);
		if (song == null) {
			logger.warn("Song not found for userId={}, songId={}", userId, songId);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		byte[] data = pdfService.getPDF(song);
		logger.info("Successfully generated PDF for song '{}'", song.title());

		return ResponseEntity.ok(new PDFDto(song.title(), data));
	}

	@GetMapping("/preview/{userId}/{songId}")
	public ResponseEntity<byte[]> previewPDF(@PathVariable Long userId, @PathVariable Long songId) throws IOException, DocumentException {
		logger.info("Received request to preview PDF for userId={}, songId={}", userId, songId);

		Song song = songService.fetchSong(userId, songId);
		if (song == null) {
			logger.warn("Song not found for preview: userId={}, songId={}", userId, songId);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		byte[] data = pdfService.getPDF(song);
		logger.info("Streaming PDF preview for song '{}'", song.title());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDisposition(ContentDisposition.inline().filename(song.title() + ".pdf").build());

		return new ResponseEntity<>(data, headers, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<PDFDto> getPDF(@RequestBody Song song) throws DocumentException, IOException {
		logger.info("Received POST request to generate PDF for song='{}'", song.title());

		byte[] data = pdfService.getPDF(song);
		logger.info("Successfully generated PDF from POST for song '{}'", song.title());

		return ResponseEntity.ok(new PDFDto(song.title(), data));
	}

	@PostMapping("/preview")
	public ResponseEntity<byte[]> previewPDF(@RequestBody Song song) throws IOException, DocumentException {
		logger.info("Received request to preview PDF via POST for song='{}'", song.title());

		byte[] data = pdfService.getPDF(song);
		logger.info("Streaming PDF preview for song '{}'", song.title());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDisposition(ContentDisposition.inline().filename(song.title() + ".pdf").build());

		return new ResponseEntity<>(data, headers, HttpStatus.OK);
	}
}
