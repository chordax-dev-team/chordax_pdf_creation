package chordax_dev_team.chordax_pdf_creation.service;

import chordax_dev_team.chordax_pdf_creation.model.Song;
import chordax_dev_team.chordax_pdf_creation.utils.PDFCreator;
import com.itextpdf.text.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PDFService {

    private static final Logger logger = LoggerFactory.getLogger(PDFService.class);

    private final PDFCreator pdfCreator;

    public PDFService(PDFCreator pdfCreator) {
        this.pdfCreator = pdfCreator;
    }

    public byte[] getPDF(Song song) throws IOException, DocumentException {
        logger.info("Generating PDF for song '{}'", song.title());

        try {
            byte[] pdfBytes = pdfCreator.createPdf(song);
            logger.info("PDF generation successful for song '{}', size={} bytes", song.title(), pdfBytes.length);
            return pdfBytes;
        } catch (IOException | DocumentException e) {
            logger.error("PDF generation failed for song '{}'", song.title(), e);
            throw e;
        }
    }
}