package wallet.dto;

import jakarta.validation.constraints.Positive;

public class DepositRequestDto {
    @Positive(message = "Deposit amount must be positive")
    private Double amount;  

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

}
