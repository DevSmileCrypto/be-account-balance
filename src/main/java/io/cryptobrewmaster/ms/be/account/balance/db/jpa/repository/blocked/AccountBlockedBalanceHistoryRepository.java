package io.cryptobrewmaster.ms.be.account.balance.db.jpa.repository.blocked;

import io.cryptobrewmaster.ms.be.account.balance.db.jpa.entity.balance.blocked.AccountBlockedBalanceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountBlockedBalanceHistoryRepository extends JpaRepository<AccountBlockedBalanceHistory, Long> {

}