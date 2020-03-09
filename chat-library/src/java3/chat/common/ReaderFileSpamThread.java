package java3.chat.common;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

public class ReaderFileSpamThread extends Thread {
    private CopyOnWriteArrayList<String> listSpam;

    public ReaderFileSpamThread(CopyOnWriteArrayList<String> listWordsSpam) {
        this.listSpam = listWordsSpam;
        start();
    }

    @Override
    public void run() {
        String FILE = "bad_words.txt";
        try (Scanner scanner = new Scanner(Paths.get(FILE), StandardCharsets.UTF_8.name())) {
            while (scanner.hasNext()) {
                listSpam.add(scanner.useDelimiter(", ").next());
            }
        } catch (IOException e) {
        }
    }
}
