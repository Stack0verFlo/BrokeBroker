package gui;

import javax.swing.*;

import controllers.UserController;
import services.UserService;
import java.awt.*;

public class LoginDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JButton loginButton, registerButton;
    private UserController userController;

    public LoginDialog(JFrame parent, UserService userService) {
        super(parent, "Login", true);
        //this.userService = userService;
        this.userController = new UserController(userService);

        setSize(300, 200);
        setLayout(new GridLayout(5, 2));

        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        // Registrierungsfelder
        add(new JLabel("Email: (Register)"));
        emailField = new JTextField();
        add(emailField);

        loginButton = new JButton("Login");
        loginButton.addActionListener(e -> authenticate());
        add(loginButton);

        registerButton = new JButton("Register");
        registerButton.addActionListener(e -> register());
        add(registerButton);

        setLocationRelativeTo(parent);
    }

    private void authenticate() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        if (userController.authenticate(username, password)) {
            // Authentifizierung erfolgreich
            JOptionPane.showMessageDialog(this, "Login successful.", "Success", JOptionPane.INFORMATION_MESSAGE);
            MainFrame mainFrame = (MainFrame) getParent();
            mainFrame.refreshOnLogin(); // Aktualisiere das Hauptfenster
            mainFrame.setVisible(true); // Zeige das Hauptfenster an
            dispose(); // Schließe den LoginDialog
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void register() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String email = emailField.getText();
        String userId = userController.register(username, email, password);
        if (userId != null) {
            JOptionPane.showMessageDialog(this, "Registration successful. Please login.", "Registered", JOptionPane.INFORMATION_MESSAGE);
            usernameField.setText(username);
            passwordField.setText(password);
            emailField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed or username already exists.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
