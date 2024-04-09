package Logging;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileLogger implements Logger {
    private final String logFilePath = "log.txt";
    @Override
    public void log(String message, LogLevel level) {
        createLogFile();
        try (PrintWriter out = new PrintWriter(new FileWriter(logFilePath, true))) {
            out.println(level.toString() + ": " + message);
        } catch (IOException e) {
            System.err.println("Fehler beim Loggen in die Datei: " + e.getMessage());
        }
    }
    public void createLogFile() {
        Path path = Paths.get(logFilePath);
        try {
            if (!Files.exists(path)) { // Überprüfen, ob die Datei existiert
                Files.createFile(path); // Datei erstellen, wenn sie nicht existiert
            }
            // Wenn die Datei bereits existiert, wird nichts unternommen,
            // das Logging kann fortgeführt werden.
        } catch (IOException e) {
            System.err.println("Fehler beim Überprüfen oder Erstellen der Log-Datei: " + e.getMessage());
        }
    }
}
