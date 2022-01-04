package io.cryptobrewmaster.ms.be.account.balance.db.jpa.repository;

import io.cryptobrewmaster.ms.be.account.balance.db.jpa.entity.balance.AccountBalance;
import io.cryptobrewmaster.ms.be.library.constants.Currency;
import io.cryptobrewmaster.ms.be.library.exception.ParametersAbsentOrInvalidException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;

public interface AccountBalanceRepository extends JpaRepository<AccountBalance, Long>, JpaSpecificationExecutor<AccountBalance> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    Optional<AccountBalance> findWithLockByAccountIdAndCurrency(String accountId, Currency currency);

    Optional<AccountBalance> findByAccountIdAndCurrency(String accountId, Currency currency);

    default <T> T getByAccountIdAndCurrency(String accountId, Currency currency, Function<AccountBalance, T> map) {
        return findByAccountIdAndCurrency(accountId, currency)
                .map(map)
                .orElseThrow(() -> new ParametersAbsentOrInvalidException(
                        String.format("Account balance with account id = %s and currency = %s not exists in system",
                                accountId, currency)
                ));
    }

    @Modifying
    @Query("UPDATE AccountBalance ab SET ab.quantity = ab.quantity + :quantity WHERE ab.accountId = :accountId AND ab.currency = :currency")
    void addToAccountBalance(@Param("accountId") String accountId, @Param("currency") Currency currency, @Param("quantity") BigDecimal quantity);

    @Modifying
    @Query("UPDATE AccountBalance ab SET ab.quantity = ab.quantity - :quantity WHERE ab.accountId = :accountId AND ab.currency = :currency")
    void subtractFromAccountBalance(@Param("accountId") String accountId, @Param("currency") Currency currency, @Param("quantity") BigDecimal quantity);

}