package gui;

import javax.swing.*;
import controllers.UserController;
import java.awt.*;

public class LoginDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;  // Nur für die Registrierung
    private JButton loginButton, registerButton;

    public LoginDialog(JFrame parent) {
        super(parent, "Login", true);
        setSize(300, 200);
        setLayout(new GridLayout(4, 2));
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
        if (new UserController().authenticate(username, password)) {
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void register() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String email = emailField.getText();
        if (new UserController().register(username, email, password)) {
            JOptionPane.showMessageDialog(this, "Registration successful. Please login.", "Registered", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
