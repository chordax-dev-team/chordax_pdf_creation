package chordax_dev_team.chordax_pdf_creation.model;

import lombok.Data;

import java.util.List;

@Data
public class Song {

	private String title;

	private String composer;

	private String author;

	private long userId;

	private List<Line> lines;
}