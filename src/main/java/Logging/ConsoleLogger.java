package Logging;

public class ConsoleLogger implements Logger{
    @Override
    public void log(String message, LogLevel level) {
        // Implementierung für das Loggen auf der Konsole
        System.out.println(level.toString() + ": " + message);
    }
}
