package wallet.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;
    private double amount;
    private String type; // "DEPOSIT" or "WITHDRAWAL"
    private LocalDateTime timestamp;

    public Transaction() {
    }

    public Transaction(Wallet wallet, double amount, String type) {
        this.wallet = wallet;
        this.amount = amount;
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

}
