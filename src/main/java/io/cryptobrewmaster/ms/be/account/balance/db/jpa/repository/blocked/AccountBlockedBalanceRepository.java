package io.cryptobrewmaster.ms.be.account.balance.db.jpa.repository.blocked;

import io.cryptobrewmaster.ms.be.account.balance.db.jpa.entity.balance.blocked.AccountBlockedBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface AccountBlockedBalanceRepository extends JpaRepository<AccountBlockedBalance, Long> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    Optional<AccountBlockedBalance> findWithLockByIdAndAccountId(Long id, String accountId);

}