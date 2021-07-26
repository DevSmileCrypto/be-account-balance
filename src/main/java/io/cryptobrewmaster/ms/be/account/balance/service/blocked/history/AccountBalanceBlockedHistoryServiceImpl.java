package io.cryptobrewmaster.ms.be.account.balance.service.blocked.history;

import io.cryptobrewmaster.ms.be.account.balance.db.entity.balance.blocked.AccountBalanceBlocked;
import io.cryptobrewmaster.ms.be.account.balance.db.entity.balance.blocked.AccountBalanceBlockedHistory;
import io.cryptobrewmaster.ms.be.account.balance.db.repository.blocked.AccountBalanceBlockedHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccountBalanceBlockedHistoryServiceImpl implements AccountBalanceBlockedHistoryService {

    private final AccountBalanceBlockedHistoryRepository accountBalanceBlockedHistoryRepository;

    @Override
    public void saveHistory(AccountBalanceBlocked accountBalanceBlocked) {
        var accountBlockedBalanceHistory = AccountBalanceBlockedHistory.of(accountBalanceBlocked);
        accountBalanceBlockedHistoryRepository.save(accountBlockedBalanceHistory);
    }
}
