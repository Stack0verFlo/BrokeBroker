import javax.swing.*;
import java.awt.*;

class MainApp {
    private JFrame frame;

    public MainApp() {
        this.frame = new JFrame("Window title");
        this.frame.setSize(1000, 1000);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setLayout(new FlowLayout(FlowLayout.CENTER));
        renderFrame();
    }

    private void renderFrame() {
        JPanel panel = new JPanel(new GridLayout(1, 1));
        this.frame.setContentPane(panel);
        this.frame.pack();
        this.frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginController loginController = new LoginController();
            }
        });
    }
}