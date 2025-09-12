package chordax_dev_team.chordax_pdf_creation.model.enums;

public enum Chord {
    C("C"), Cm("Cm"), C7("C7"), CMaj7("CMaj7"), Cm7("Cm7"), Cdim("Cdim"), Caug("Caug"),
    C_SHARP("C♯"), C_SHARPm("C♯m"), C_SHARP7("C♯7"), C_SHARPMaj7("C♯Maj7"), C_SHARPm7("C♯m7"), C_SHARPdim("C♯dim"), C_SHARPaug("C♯aug"),
    D("D"), Dm("Dm"), D7("D7"), DMaj7("DMaj7"), Dm7("Dm7"), Ddim("Ddim"), Daug("Daug"),
    D_SHARP("D♯"), D_SHARPm("D♯m"), D_SHARP7("D♯7"), D_SHARPMaj7("D♯Maj7"), D_SHARPm7("D♯m7"), D_SHARPdim("D♯dim"), D_SHARPaug("D♯aug"),
    E("E"), Em("Em"), E7("E7"), EMaj7("EMaj7"), Em7("Em7"), Edim("Edim"), Eaug("Eaug"),
    F("F"), Fm("Fm"), F7("F7"), FMaj7("FMaj7"), Fm7("Fm7"), Fdim("Fdim"), Faug("Faug"),
    F_SHARP("F♯"), F_SHARPm("F♯m"), F_SHARP7("F♯7"), F_SHARPMaj7("F♯Maj7"), F_SHARPm7("F♯m7"), F_SHARPdim("F♯dim"), F_SHARPaug("F♯aug"),
    G("G"), Gm("Gm"), G7("G7"), GMaj7("GMaj7"), Gm7("Gm7"), Gdim("Gdim"), Gaug("Gaug"),
    G_SHARP("G♯"), G_SHARPm("G♯m"), G_SHARP7("G♯7"), G_SHARPMaj7("G♯Maj7"), G_SHARPm7("G♯m7"), G_SHARPdim("G♯dim"), G_SHARPaug("G♯aug"),
    A("A"), Am("Am"), A7("A7"), AMaj7("AMaj7"), Am7("Am7"), Adim("Adim"), Aaug("Aaug"),
    A_SHARP("A♯"), A_SHARPm("A♯m"), A_SHARP7("A♯7"), A_SHARPMaj7("A♯Maj7"), A_SHARPm7("A♯m7"), A_SHARPdim("A♯dim"), A_SHARPaug("A♯aug"),
    B("B"), Bm("Bm"), B7("B7"), BMaj7("BMaj7"), Bm7("Bm7"), Bdim("Bdim"), Baug("Baug");

    private final String label;

    Chord(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}