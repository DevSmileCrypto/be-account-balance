package io.cryptobrewmaster.ms.be.account.balance.db.repository.blocked;

import io.cryptobrewmaster.ms.be.account.balance.db.entity.blocked.AccountBalanceBlockedHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountBalanceBlockedHistoryRepository extends JpaRepository<AccountBalanceBlockedHistory, Long> {

}