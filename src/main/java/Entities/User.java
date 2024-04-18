package Entities;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String id; // Eindeutige ID (z.B. generiert von MongoDB)
    private String name;
    private String email;
    private String password; // Beachten Sie die Sicherheitshinweise unten
    // Annahme, dass jedes Portfolio eine eindeutige ID hat oder direkt als Objekt referenziert wird

    private double balance; // Kontostand des Benutzers
    private List<String> portfolioIds; // Referenzen auf Portfolios, falls mehrere Portfolios unterstützt werden

    public User(String name, String email, String password, double balance) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.balance = balance;
        this.portfolioIds = new ArrayList<>();
    }

    // Getter und Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getPortfolioIds() {
        return portfolioIds;
    }

    public void setPortfolioIds(List<String> portfolioIds) {
        this.portfolioIds = portfolioIds;
    }

    public double getBalance() {
        return balance;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }

    // Methode zum Hinzufügen eines Portfolios zum Benutzer
    public void addPortfolioId(String portfolioId) {
        if (!portfolioIds.contains(portfolioId)) {
            portfolioIds.add(portfolioId);
        }
    }
}
