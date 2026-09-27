package wallet.dto;

import java.time.LocalDateTime;

public class TransactionDto {

    private Long id;
    private String type;
    private Double amount;
    private LocalDateTime timestamp;

    public TransactionDto(
            Long id,
            String type,
            Double amount,
            LocalDateTime timestamp) {

        this.id = id;
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Double getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}