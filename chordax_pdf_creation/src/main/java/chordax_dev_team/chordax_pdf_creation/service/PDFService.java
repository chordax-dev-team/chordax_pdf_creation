package chordax_dev_team.chordax_pdf_creation.service;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.itextpdf.text.DocumentException;

//Interface
@Service
public interface PDFService {

	ResponseEntity<byte[]> getResponseWithPDF(long songId) throws IOException, DocumentException, InterruptedException;
}
