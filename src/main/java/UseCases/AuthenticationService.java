package UseCases;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationService {
    private static final String USER_CREDENTIALS_FILE = "userCredentials.csv";
    private Map<String, String> userCredentials = new HashMap<>();

    public AuthenticationService() {
        loadUserCredentials();
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

    public boolean authenticateUser(String username, String password) {
        String storedPassword = userCredentials.get(username);
        return storedPassword != null && storedPassword.equals(password);
    }

    public void registerUser(String username, String password) {
        if (!userCredentials.containsKey(username)) {
            userCredentials.put(username, password);
            saveUserCredentials(username, password);
        }
    }

    private void saveUserCredentials(String username, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_CREDENTIALS_FILE, true))) {
            writer.write(username + "," + password + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
