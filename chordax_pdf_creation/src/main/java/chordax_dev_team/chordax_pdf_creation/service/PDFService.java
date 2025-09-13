package chordax_dev_team.chordax_pdf_creation.service;

import chordax_dev_team.chordax_pdf_creation.model.Song;
import chordax_dev_team.chordax_pdf_creation.utils.PDFCreator;
import com.itextpdf.text.DocumentException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class PDFService {

	public byte[] getPDF(Song song) throws IOException, DocumentException {
        File pdfFile = new PDFCreator().createPdf(song);
        Path pdfPath = pdfFile.toPath();
        byte[] pdfData = Files.readAllBytes(pdfPath);

        boolean deleted = pdfFile.delete();
        if (!deleted) {
            System.err.println("Warning: Temporary PDF file could not be deleted: " + pdfFile.getPath());
        }
        return pdfData;
    }
}