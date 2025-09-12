package chordax_dev_team.chordax_pdf_creation.utils;

import java.io.File;
import java.util.Objects;

public class FileUtils {

    public static String[] getFileNamesInFolder(String folderPath) {

        return java.util.Arrays.stream(getFiles(folderPath))
                .filter(File::isFile)
                .map(File::getName)
                .toArray(String[]::new);
    }

    private static File[] getFiles(String folderPath){

        File folder = new File(folderPath);

        if (!folder.exists() || !folder.isDirectory()) {
            throw new IllegalArgumentException("Invalid folder path: " + folderPath);
        }

        File[] files = folder.listFiles();

        return Objects.requireNonNullElseGet(files, () -> new File[0]);
    }
}