package chordax_dev_team.chordax_pdf_creation.service;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import chordax_dev_team.chordax_pdf_creation.model.Song;

public class JsonParser {

	private String fileName;

	private final static String FILE_PATH = "src/main/resources/static/songs/";

	public JsonParser(String fileName) {
		this.fileName = fileName;
	}

	public Song convertToJson() {

		Song songJson = null;
		
	// from stackoverflow.com/questions/11700482/convert-text-file-to-json-in-java
		ObjectMapper mapper = new ObjectMapper();
		try {
			File jsonInputFile = new File(FILE_PATH + fileName);
			songJson = mapper.readValue(jsonInputFile, Song.class);			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return songJson;
	}
}
