package chordax_dev_team.chordax_pdf_creation.model;

import chordax_dev_team.chordax_pdf_creation.model.enums.LineType;
import java.util.List;

public record Line(LineType lineType, String lyrics, List<Tone> tones) {}