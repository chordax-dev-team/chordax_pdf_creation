package chordax_dev_team.chordax_pdf_creation.service;

import chordax_dev_team.chordax_pdf_creation.model.Song;
import com.itextpdf.text.DocumentException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PDFServiceImpl implements PDFService {

	@Override
	public byte[] getPDF(long songId) throws IOException, DocumentException, InterruptedException {
		JsonParser parser = new JsonParser(songId);
		Song song = parser.convertToJson();

		if (song == null) {
			throw new IOException("Failed to parse song JSON: " + parser.getFileName());
		}

		return PDFFetcher.getPDF(song);
	}
}
