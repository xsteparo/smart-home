package cz.cvut.fel.omo.util.logger;

import java.io.File;

/**
 * The type Logger cleaner.
 */
public class LoggerCleaner {

    public static void clearLogsFolder(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
//            System.out.println("Folder " + folderPath + " not found");
            return;
        }

        File[] files = folder.listFiles();
        if (files == null) {
//            System.out.println("Can't get files from folder: " + folderPath);
            return;
        }

        for (File file : files) {
            if (!file.isDirectory() && file.getName().endsWith(".log")) {
                boolean isDeleted = file.delete();
                if (isDeleted) {
//                    System.out.println("File deleted: " + file.getPath());
                } else {
//                    System.out.println("Cleaner didn't delete files: " + file.getPath());
                }
            }
        }
    }
}
