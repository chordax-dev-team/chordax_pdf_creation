package chordax_dev_team.chordax_pdf_creation.titles.utils;

import chordax_dev_team.chordax_pdf_creation.model.Song;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class FileUtils {

    private static final String FILE_PATH = "src/main/resources/static/songs/";

    public static List<String> getSongTitlesFromFiles() {

        return java.util.Arrays.stream(getFiles())
                .filter(File::isFile)
                .map(file-> {
                    try {
                        return new ObjectMapper().readValue(file, Song.class);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).map(Song::title).toList();
    }

    private static File[] getFiles(){

        File folder = new File(FILE_PATH);

        if (!folder.exists() || !folder.isDirectory()) {
            throw new IllegalArgumentException("Invalid folder path: " + FILE_PATH);
        }

        File[] files = folder.listFiles();

        return Objects.requireNonNullElseGet(files, () -> new File[0]);
    }
}