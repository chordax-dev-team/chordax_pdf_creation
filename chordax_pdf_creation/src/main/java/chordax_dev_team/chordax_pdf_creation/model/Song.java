package chordax_dev_team.chordax_pdf_creation.model;

import java.util.List;

public record Song(String title, String composer, String author, long userId, List<Line> lines) {}