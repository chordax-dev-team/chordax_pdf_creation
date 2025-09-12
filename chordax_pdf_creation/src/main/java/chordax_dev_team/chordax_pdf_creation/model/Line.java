package chordax_dev_team.chordax_pdf_creation.model;

import chordax_dev_team.chordax_pdf_creation.model.enums.LineType;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
public class Line {

	private LineType lineType;

	private String lyrics;

	private List<Tone> tones;

}
