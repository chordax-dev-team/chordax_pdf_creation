package chordax_dev_team.chordax_pdf_creation.titles.service;

import chordax_dev_team.chordax_pdf_creation.titles.utils.FileUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TitlesService {

    public List<String> getTitles(){
        return FileUtils.getSongTitlesFromFiles();
    }
}