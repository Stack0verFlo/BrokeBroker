package gui;

import javax.swing.*;

import controllers.UserController;
import services.UserService;
import java.awt.*;
import java.net.URL;

public class LoginDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JButton loginButton, registerButton;
    private UserController userController;

    public LoginDialog(JFrame parent, UserService userService) {
        super(parent, "Login", true);
        this.userController = new UserController(userService);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        getContentPane().add(mainPanel);
        addLogo(mainPanel);
        addLoginForm(mainPanel);
        pack();
        setLocationRelativeTo(parent);
    }
    private void addLogo(JPanel mainPanel) {
        URL imageUrl = getClass().getResource("/img/brokebroker.png");
        assert imageUrl != null;
        ImageIcon logoIcon = new ImageIcon(imageUrl);
        Image image = logoIcon.getImage();
        Image newImg = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        logoIcon = new ImageIcon(newImg);

        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(logoLabel);
        mainPanel.add(Box.createVerticalStrut(10));
    }
    private void addLoginForm(JPanel mainPanel) {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0, 2, 10, 10));
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        emailField = new JTextField();

        formPanel.add(new JLabel("Username:"));
        formPanel.add(usernameField);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);
        formPanel.add(new JLabel("Email (Register):"));
        formPanel.add(emailField);

        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        loginButton.addActionListener(e -> authenticate());
        registerButton.addActionListener(e -> register());

        mainPanel.add(formPanel);
        mainPanel.add(buttonPanel);
    }

    private void authenticate() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        if (userController.authenticate(username, password)) {
            JOptionPane.showMessageDialog(this, "Login successful.", "Success", JOptionPane.INFORMATION_MESSAGE);
            MainFrame mainFrame = (MainFrame) getParent();
            mainFrame.refreshOnLogin();
            mainFrame.setVisible(true);
            dispose();
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
