package chordax_dev_team.chordax_pdf_creation.service;

import com.itextpdf.text.DocumentException;
import java.io.IOException;

//Interface
public interface PDFService {
	byte[] getPDF(long songId) throws IOException, DocumentException, InterruptedException;
}
