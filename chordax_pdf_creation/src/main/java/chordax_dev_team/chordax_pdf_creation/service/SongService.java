package chordax_dev_team.chordax_pdf_creation.service;

import chordax_dev_team.chordax_pdf_creation.model.Song;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class SongService {

    private static final Logger logger = LoggerFactory.getLogger(SongService.class);
    private final DiscoveryClient discoveryClient;
    private final RestTemplate restTemplate = new RestTemplate();

    public SongService(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    public Song fetchSong(Long userId, Long songId) {
        List<ServiceInstance> instances = discoveryClient.getInstances("chordax_songs");

        if (instances.isEmpty()) {
            logger.error("No instances found for service: chordax_songs");
            return null;
        }

        String serviceURI = String.format("%s/api/v1/songs/%d/%d", instances.get(0).getUri(), userId, songId);
        logger.info("Fetching song from URI: {}", serviceURI);

        try {
            ResponseEntity<Song> response = restTemplate.getForEntity(serviceURI, Song.class);
            return response.getBody();
        } catch (Exception e) {
            logger.error("Failed to fetch song from external service", e);
            return null;
        }
    }
}