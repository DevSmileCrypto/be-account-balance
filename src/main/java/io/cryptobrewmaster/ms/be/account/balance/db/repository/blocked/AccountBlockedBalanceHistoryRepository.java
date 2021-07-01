package io.cryptobrewmaster.ms.be.account.balance.db.repository.blocked;

import io.cryptobrewmaster.ms.be.account.balance.db.entity.blocked.AccountBlockedBalanceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountBlockedBalanceHistoryRepository extends JpaRepository<AccountBlockedBalanceHistory, Long> {

}