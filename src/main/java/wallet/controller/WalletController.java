package wallet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import wallet.dto.DepositRequestDto;
import wallet.dto.TransactionDto;
import wallet.dto.WalletDto;
import wallet.dto.WithdrawRequestDto;
import wallet.model.Wallet;
import wallet.service.WalletService;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PostMapping("/create")
    public Wallet createWallet() {
        return walletService.createWallet();
    }

    @GetMapping("/{id}")
    public WalletDto getWallet(@PathVariable Long id) {
        return walletService.getWalletDto(id);
    }

    @PostMapping("/{id}/deposit")
    public WalletDto deposit(@PathVariable Long id, @Valid @RequestBody DepositRequestDto requestDto) {
        Wallet wallet = walletService.deposit(id, requestDto.getAmount());
        return new WalletDto(wallet.getId(), wallet.getBalance());
    }

    @GetMapping("/{id}/transactions")
    public List<TransactionDto> getTransactions(@PathVariable Long id) {

        return walletService.getTransactions(id)
                .stream()
                .map(transaction -> new TransactionDto(
                        transaction.getId(),
                        transaction.getType(),
                        transaction.getAmount(),
                        transaction.getTimestamp()))
                .toList();
    }

    @PostMapping("/{id}/withdraw")
    public WalletDto withdraw(
            @PathVariable Long id,
            @Valid @RequestBody WithdrawRequestDto request) {

        Wallet wallet = walletService.withdraw(id, request.getAmount());

        return new WalletDto(
                wallet.getId(),
                wallet.getBalance());
    }

}