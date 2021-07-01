package io.cryptobrewmaster.ms.be.account.balance.db.repository.blocked;

import io.cryptobrewmaster.ms.be.account.balance.db.entity.blocked.AccountBlockedBalance;
import io.cryptobrewmaster.ms.be.library.exception.ParametersAbsentOrInvalidException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface AccountBlockedBalanceRepository extends JpaRepository<AccountBlockedBalance, Long> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    Optional<AccountBlockedBalance> findWithLockByIdAndAccountBalance_AccountId(Long id, String accountId);

    default AccountBlockedBalance getWithLockByIdAndAccountId(Long id, String accountId) {
        return findWithLockByIdAndAccountBalance_AccountId(id, accountId)
                .orElseThrow(() -> new ParametersAbsentOrInvalidException(
                        String.format("Account blocked balance with id = %s and account id = %s not exists", id, accountId)
                ));
    }

}