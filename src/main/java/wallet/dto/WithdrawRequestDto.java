package wallet.dto;

import jakarta.validation.constraints.Positive;

public class WithdrawRequestDto {

    @Positive(message = "Amount must be greater than 0")
    private Double amount;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}