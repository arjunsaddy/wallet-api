package wallet.dto;

public class WalletDto {
    private Long id;
    private Double balance;

    public WalletDto(Long id, Double balance) {
        this.id = id;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public Double getBalance() {
        return balance;
    }

}
