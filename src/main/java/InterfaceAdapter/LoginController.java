package InterfaceAdapter;

import Frameworks.MainApp;
import UseCases.AuthenticationService;

import javax.swing.*;
import java.awt.*;

public class LoginController {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private AuthenticationService authService;

    public LoginController() {
        authService = new AuthenticationService(); // Initialisiere den Authentifizierungsdienst
        initializeFrame();
        addLogo();
        addLoginPanel();
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void initializeFrame() {
        frame = new JFrame("BrokeBroker Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10)); // Setzt Abstände zwischen den Komponenten
    }

    private void addLogo() {
        // Logo einfügen
        ImageIcon logoIcon = new ImageIcon("img/brokebroker.png");
        Image image = logoIcon.getImage();
        Image newimg = image.getScaledInstance(90, 90, Image.SCALE_SMOOTH);
        logoIcon = new ImageIcon(newimg);
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setHorizontalAlignment(JLabel.CENTER);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(logoLabel, BorderLayout.NORTH);
    }

    private void addLoginPanel() {
        // Panel für Login-Felder
        JPanel loginPanel = new JPanel();
        loginPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));

        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        JButton loginButton = new JButton("Einloggen");
        JButton registerButton = new JButton("Registrieren");

        loginButton.addActionListener(e -> performLogin());
        registerButton.addActionListener(e -> registerUser());

        loginPanel.add(new JLabel("Benutzername:"));
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel("Passwort:"));
        loginPanel.add(passwordField);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        loginPanel.add(loginButton);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        loginPanel.add(registerButton);

        frame.add(loginPanel, BorderLayout.CENTER);
    }


    // Methoden zum Initialisieren der UI-Komponenten bleiben unverändert

    private void performLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        if (authService.authenticateUser(username, password)) {
            frame.dispose(); // Schließt das Login-Fenster
            new MainApp(username); // Startet die Hauptanwendung
        } else {
            JOptionPane.showMessageDialog(frame, "Anmeldefehler: Ungültige Anmeldedaten!", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registerUser() {
        String username = JOptionPane.showInputDialog(frame, "Benutzername");
        String password = JOptionPane.showInputDialog(frame, "Passwort");
        if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
            authService.registerUser(username, password);
            JOptionPane.showMessageDialog(frame, "Benutzer erfolgreich registriert!", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
