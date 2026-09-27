package wallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wallet.model.Transaction;
import wallet.model.Wallet;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByWallet(Wallet wallet);
}