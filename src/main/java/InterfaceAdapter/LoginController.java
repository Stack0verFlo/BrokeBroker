package InterfaceAdapter;

import Frameworks.MainApp;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class LoginController {

    private final JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private final Map<String, String> userCredentials = new HashMap<>();
    private static final String USER_CREDENTIALS_FILE = "userCredentials.csv";

    public LoginController() {
        this.frame = new JFrame("BrokeBroker Login");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setLayout(new BorderLayout(10, 10)); // Setzt Abstände zwischen den Komponenten
        renderFrame();
        this.frame.pack(); // Passt die Größe dem Inhalt an
        this.frame.setLocationRelativeTo(null); // Zentriert das Fenster auf dem Bildschirm
        this.frame.setVisible(true);
    }

    private void renderFrame() {
        // Logo einfügen
        ImageIcon logoIcon = new ImageIcon("img/brokebroker.png");
        Image image = logoIcon.getImage();
        Image newimg = image.getScaledInstance(90, 90, Image.SCALE_SMOOTH);
        logoIcon = new ImageIcon(newimg);
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setHorizontalAlignment(JLabel.CENTER);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel für Login-Felder
        JPanel loginPanel = new JPanel();
        loginPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));

        JLabel usernameLabel = new JLabel("Benutzername:");
        usernameField = new JTextField(15);

        JLabel passwordLabel = new JLabel("Passwort:");
        passwordField = new JPasswordField(15);

        JButton loginButton = new JButton("Einloggen");
        loginButton.addActionListener(e -> performLogin());

        JButton registerButton = new JButton("Registrieren");
        registerButton.addActionListener(e -> registerUser());

        // Komponenten zentrieren
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Komponenten zum Panel hinzufügen
        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        loginPanel.add(loginButton);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        loginPanel.add(registerButton);

        // Fügt das Logo und das Login-Panel zum Frame hinzu
        this.frame.add(logoLabel, BorderLayout.NORTH);
        this.frame.add(loginPanel, BorderLayout.CENTER);
    }
    private void registerUser() {
        String username = JOptionPane.showInputDialog(frame, "Benutzername");
        String password = JOptionPane.showInputDialog(frame, "Passwort");
        if (isValidInput(username, password)) {
            saveUserCredentials(username, password);
            JOptionPane.showMessageDialog(frame, "Benutzer erfolgreich registriert!", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    private boolean isValidInput(String username, String password) {
        return username != null && password != null && !username.isEmpty() && !password.isEmpty();
    }

    public void saveUserCredentials(String username, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("userCredentials.csv", true))) {
            writer.write(username + "," + password + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadUserCredentials() {
        File file = new File(USER_CREDENTIALS_FILE);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] credentials = line.split(",");
                    if (credentials.length == 2) {
                        userCredentials.put(credentials[0], credentials[1]);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private boolean authenticateUser(String username, String password) {
        loadUserCredentials();
        String storedPassword = userCredentials.get(username);
        return storedPassword != null && storedPassword.equals(password);
    }

    private void performLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        if (authenticateUser(username, password)) {
            frame.dispose(); // Schließt das Login-Fenster
            new MainApp(username); // Startet die Frameworks.MainApp mit dem Benutzernamen
        } else {
            JOptionPane.showMessageDialog(frame, "Anmeldefehler: Ungültige Anmeldedaten!", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }
}
