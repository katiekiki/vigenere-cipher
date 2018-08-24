package exercise.vigenere;

import java.io.*;
import java.nio.file.*;
import java.util.Optional;

public class VigenereFileCipher {

    private VigenereCipher vigenereCipher;
    private String inputDirectory;
    private String outputDirectory;
    private String fileExtensionFilter;
    private CopyFileVisitor copyFileVisitor;
    private boolean encrypt;

    public VigenereFileCipher(String sourceCharacterSet, String key, String inputDirectory, String fileExtensionFilter) {
        this.vigenereCipher = new VigenereCipher(sourceCharacterSet, key);
        this.inputDirectory = inputDirectory;
        this.fileExtensionFilter = fileExtensionFilter;

    }

    public void encryptFiles() throws IOException {
        this.outputDirectory = this.inputDirectory + ".encrypted";
        this.copyFileVisitor = new CopyFileVisitor(Paths.get(this.outputDirectory));
        this.encrypt = true;
        processFiles();
    }

    public void decryptFiles() throws IOException {
        this.outputDirectory = this.inputDirectory + ".decrypted";
        this.copyFileVisitor = new CopyFileVisitor(Paths.get(this.outputDirectory));
        this.encrypt = false;
        processFiles();
    }



    private void processFiles() throws IOException {

        //create output directory if it doesn't already exist
        Path path = Paths.get(this.outputDirectory);
        if(!Files.exists(path)) {
            Files.createDirectory(path);
        }

        //copy input files to output folder
        Files.walkFileTree(Paths.get(this.inputDirectory), this.copyFileVisitor);

        //crawl the directories to encrypt/decrypt files.
        crawl(new File(this.outputDirectory));

    }


    public void processSingleFile(File file) {
        String readLine;

        Optional<String> fileExtension = copyFileVisitor.getExtensionByStringHandling(file.getName());

        //write the output to a temp file
        File tempOutputFile = new File(file.getAbsolutePath() + "_temp");

        if (fileExtension.isPresent() && fileExtension.get().equals(fileExtensionFilter)) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                 Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempOutputFile), "utf-8"))
            ) {
                while ((readLine = bufferedReader.readLine()) != null) {
                    String updatedLine;
                    if (encrypt) {
                        updatedLine = this.vigenereCipher.encrypt(readLine);
                    } else {
                        updatedLine = this.vigenereCipher.decrypt(readLine);
                    }

                    writer.write(updatedLine);
                    writer.write(System.lineSeparator());
                    writer.flush();

                }

            } catch (FileNotFoundException fnf) {
                System.out.println("Unable to locate file in directory " + fnf);
            } catch (IOException ioe) {
                System.out.println("IOException encountered while loading file " + ioe);
            }

            //rename the temp file to original file name
            tempOutputFile.renameTo(file);
        }


    }

    public File[] listDirectories(File path) {

        return path.listFiles();

    }

    public void crawl(File path) {

        File[] files = listDirectories(path);

        if (files != null) {
            for (File file: files) {
                if (file.isFile()) {
                    //encrypt/decrypt each file
                    processSingleFile(file);
                }
                //then continue to process subfolders
                crawl(file);
            }
        }


    }



}
