package helloworld;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LocalDataFetcher implements DataFetcher {
    private static final Logger LOGGER = Logger.getLogger(LocalDataFetcher.class.getName());

    @Override
    public ArrayList<File> downloadPackage(String url, boolean isLambdaEnvironment) {
        ArrayList<File> downloadedFiles = new ArrayList<>();

        try {
            if (url.startsWith("file://")) {
                url = url.substring(7);
            }

            Path sourceDirectory = new File(url).toPath();
            String targetPath = isLambdaEnvironment ? "/tmp" : "temp";
            Path targetDirectory = new File(targetPath).toPath();

            if (!Files.exists(targetDirectory)) {
                Files.createDirectories(targetDirectory);
            }

            Path finalTargetDirectory = targetDirectory;
            Files.walkFileTree(sourceDirectory, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    Path targetDir = finalTargetDirectory.resolve(sourceDirectory.relativize(dir));
                    Files.createDirectories(targetDir);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Path targetFile = finalTargetDirectory.resolve(sourceDirectory.relativize(file));
                    Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
                    downloadedFiles.add(targetFile.toFile());
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while copying the file: ", e);
        }

        return downloadedFiles;
    }
}
