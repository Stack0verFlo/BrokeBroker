import gui.LoginDialog;
import gui.MainFrame;

import static gui.MainFrame.getUserService;


public class Main {

    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();
        LoginDialog loginDialog = new LoginDialog(mainFrame, getUserService());
        loginDialog.setVisible(true);

        if (!loginDialog.isDisplayable()) {
            mainFrame.refreshOnLogin();
            mainFrame.setVisible(true);
        } else {
            System.exit(0);
        }
    }
}
