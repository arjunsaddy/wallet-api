package wallet.service;

import org.springframework.stereotype.Service;

import java.util.List;

import wallet.dto.WalletDto;
import wallet.exception.WalletException;
import wallet.model.Transaction;
import wallet.model.Wallet;
import wallet.repository.WalletRepository;
import wallet.repository.TransactionRepository;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    public WalletService(WalletRepository walletRepository, TransactionRepository transactionRepository) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
    }

    public Wallet createWallet() {
        Wallet wallet = new Wallet(0.0);
        return walletRepository.save(wallet);
    }

    public Wallet getWallet(Long id) {
        return walletRepository.findById(id).orElseThrow(() -> new WalletException("Wallet not found"));
    }

    public Wallet deposit(Long id, Double amount) {

        Wallet wallet = getWallet(id);

        wallet.setBalance(wallet.getBalance() + amount);

        transactionRepository.save(new Transaction(wallet, amount, "DEPOSIT"));

        return walletRepository.save(wallet);
    }

    public Wallet withdraw(Long id, Double amount) {

        Wallet wallet = getWallet(id);

        if (wallet.getBalance() < amount) {
            throw new WalletException("Insufficient funds");
        }

        wallet.setBalance(wallet.getBalance() - amount);

        transactionRepository.save(new Transaction(wallet, amount, "WITHDRAW"));

        return walletRepository.save(wallet);
    }

    public List<Transaction> getTransactions(Long walletId) {
        Wallet wallet = getWallet(walletId);
        return transactionRepository.findByWallet(wallet);
    }

    public WalletDto getWalletDto(Long id) {
        Wallet wallet = getWallet(id);
        return new WalletDto(wallet.getId(), wallet.getBalance());
    }


}