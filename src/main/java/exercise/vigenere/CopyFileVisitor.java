package exercise.vigenere;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Optional;

public class CopyFileVisitor extends SimpleFileVisitor<Path> {
    private final Path targetPath;
    private Path sourcePath = null;
    public CopyFileVisitor(Path targetPath) {
        this.targetPath = targetPath;
    }

    @Override
    public FileVisitResult preVisitDirectory(final Path dir,
                                             final BasicFileAttributes attrs) throws IOException {
        if (sourcePath == null) {
            sourcePath = dir;
        } else {
            Files.createDirectories(targetPath.resolve(sourcePath
                    .relativize(dir)));
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(final Path file,
                                     final BasicFileAttributes attrs) throws IOException {
        Optional<String> fileExtension = getExtensionByStringHandling(file.getFileName().toString());
        if (fileExtension.get().equals("txt")) {
            Files.copy(file,
                    targetPath.resolve(sourcePath.relativize(file)),StandardCopyOption.REPLACE_EXISTING);
        }
        return FileVisitResult.CONTINUE;
    }

    public Optional<String> getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

}
