package wallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wallet.model.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
}