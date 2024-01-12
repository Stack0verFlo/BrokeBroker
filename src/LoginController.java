import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class LoginController {

    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private LoginSuccessListener loginSuccessListener;
    private JButton registerButton;
    private Map<String, String> userCredentials = new HashMap<>();
    private static final String USER_CREDENTIALS_FILE = "userCredentials.csv";

    public LoginController() {
        this.frame = new JFrame("BrokeBroker Login");
        this.frame.setSize(300, 200);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setLayout(new BorderLayout());
        renderFrame();
    }

    private void renderFrame() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        panel.add(new JLabel("Benutzername:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Passwort:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        loginButton = new JButton("Einloggen");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
        panel.add(loginButton);
        registerButton = new JButton("Registrieren");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        panel.add(registerButton);

        this.frame.add(panel, BorderLayout.CENTER);
        this.frame.pack();
        this.frame.setVisible(true);
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
            new MainApp(username); // Startet die MainApp mit dem Benutzernamen
        } else {
            JOptionPane.showMessageDialog(frame, "Anmeldefehler: Ungültige Anmeldedaten!", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    public interface LoginSuccessListener {
        void onLoginSuccess();
    }

    public void setLoginSuccessListener(LoginSuccessListener listener) {
        this.loginSuccessListener = listener;
    }
}
