package chordax_dev_team.chordax_pdf_creation.titles.controller;

import chordax_dev_team.chordax_pdf_creation.titles.service.TitlesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/song-titles")
@CrossOrigin(origins = "*") // Consider restricting this in production
public class SongCollectionController {

    @Autowired
    private TitlesService titlesService;

    @GetMapping
    public ResponseEntity<List<String>> getPDF(){

        List<String> titles = titlesService.getTitles();
        return titles != null
                ? ResponseEntity.status(HttpStatus.OK).body(titles)
                : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}