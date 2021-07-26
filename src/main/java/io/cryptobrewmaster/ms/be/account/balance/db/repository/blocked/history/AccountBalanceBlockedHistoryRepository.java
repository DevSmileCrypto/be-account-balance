package io.cryptobrewmaster.ms.be.account.balance.db.repository.blocked.history;

import io.cryptobrewmaster.ms.be.account.balance.db.entity.balance.blocked.history.AccountBalanceBlockedHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountBalanceBlockedHistoryRepository extends JpaRepository<AccountBalanceBlockedHistory, Long> {

}