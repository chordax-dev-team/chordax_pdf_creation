package chordax_dev_team.chordax_pdf_creation.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import chordax_dev_team.chordax_pdf_creation.model.Song;
import chordax_dev_team.chordax_pdf_creation.utils.FileUtils;
import lombok.Getter;

import java.io.File;
import java.io.IOException;

@Getter
public class JsonParser {

	private final String fileName;
	private static final String FILE_PATH = "src/main/resources/static/songs/";

	public JsonParser(long fileIndexInArray) {
		String[] files = FileUtils.getFileNamesInFolder(FILE_PATH);
		if (fileIndexInArray < 0 || fileIndexInArray >= files.length) {
			throw new IllegalArgumentException("Invalid file index: " + fileIndexInArray);
		}
		this.fileName = files[(int) fileIndexInArray];
		System.out.println("fileName in JsonParser: " + this.fileName);
	}

	public Song convertToObject() {
		ObjectMapper mapper = new ObjectMapper();
		File jsonInputFile = new File(FILE_PATH + this.fileName);

		try {
			return mapper.readValue(jsonInputFile, Song.class);
		} catch (IOException e) {
			System.err.println("Failed to parse JSON file: " + this.fileName);
			e.printStackTrace();
			return null;
		}
	}
}