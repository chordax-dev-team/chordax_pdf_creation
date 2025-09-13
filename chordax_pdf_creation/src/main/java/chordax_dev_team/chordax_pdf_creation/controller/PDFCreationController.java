package chordax_dev_team.chordax_pdf_creation.controller;

import chordax_dev_team.chordax_pdf_creation.dto.PDFDto;
import chordax_dev_team.chordax_pdf_creation.model.Song;
import chordax_dev_team.chordax_pdf_creation.service.PDFFetcher;

import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pdfs")
@CrossOrigin(origins = "*") // Consider restricting this in production
public class PDFCreationController {


	@Autowired
	private DiscoveryClient discoveryClient;

	@GetMapping("{userId}/{songId}")
	public ResponseEntity<PDFDto> getPDF(@PathVariable("userId") Long userId, @PathVariable("songId") Long songId) throws DocumentException, IOException {

		RestTemplate restTemplate = new RestTemplate();
		List<ServiceInstance> serviceInstances = discoveryClient.getInstances("chordax_songs");
		if(serviceInstances.isEmpty()) return null;

		String serviceURI = String.format("%s/api/v1/songs/%d/%d", serviceInstances.get(0).getUri().toString(), userId, songId);
		ResponseEntity<Song> restExchange = restTemplate.exchange(serviceURI, HttpMethod.GET, null, Song.class);
		Song song = restExchange.getBody();
		String title = song.title();
		byte[] data = PDFFetcher.getPDF(song);

		return ResponseEntity.ok(new PDFDto(title,data));
	}

	@PostMapping("{userId}")
	public ResponseEntity<PDFDto> getPDF(@PathVariable("userId") Long userId, @RequestBody Song song) throws DocumentException, IOException {
		String title = song.title();
		byte[] data = PDFFetcher.getPDF(song);

		return ResponseEntity.ok(new PDFDto(title,data));
	}
}