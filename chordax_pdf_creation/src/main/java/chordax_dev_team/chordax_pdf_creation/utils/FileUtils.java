package chordax_dev_team.chordax_pdf_creation.utils;

import java.io.File;

public class FileUtils {

    public static String[] getFileNamesInFolder(String folderPath) {
        File folder = new File(folderPath);

        if (!folder.exists() || !folder.isDirectory()) {
            throw new IllegalArgumentException("Invalid folder path: " + folderPath);
        }

        File[] files = folder.listFiles();
        if (files == null) {
            return new String[0]; // Folder is empty or inaccessible
        }

        return java.util.Arrays.stream(files)
                .filter(File::isFile)
                .map(File::getName)
                .toArray(String[]::new);
    }
}
