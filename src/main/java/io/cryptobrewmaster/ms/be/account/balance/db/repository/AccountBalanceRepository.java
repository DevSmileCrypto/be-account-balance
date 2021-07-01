package io.cryptobrewmaster.ms.be.account.balance.db.repository;

import io.cryptobrewmaster.ms.be.account.balance.db.entity.AccountBalance;
import io.cryptobrewmaster.ms.be.library.constants.Currency;
import io.cryptobrewmaster.ms.be.library.dto.PageDto;
import io.cryptobrewmaster.ms.be.library.exception.ParametersAbsentOrInvalidException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface AccountBalanceRepository extends JpaRepository<AccountBalance, Long> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    Optional<AccountBalance> findWithLockByAccountIdAndCurrency(String accountId, Currency currency);

    default AccountBalance getWithLockByAccountIdAndCurrency(String accountId, Currency currency) {
        return findWithLockByAccountIdAndCurrency(accountId, currency)
                .orElseThrow(() -> new ParametersAbsentOrInvalidException(
                        String.format("Account balance with account id = %s and currency = %s not exists in system",
                                accountId, currency)
                ));
    }

    Optional<AccountBalance> findByAccountIdAndCurrency(String accountId, Currency currency);

    default <T> T getByAccountIdAndCurrency(String accountId, Currency currency, Function<AccountBalance, T> map) {
        return findByAccountIdAndCurrency(accountId, currency)
                .map(map)
                .orElseThrow(() -> new ParametersAbsentOrInvalidException(
                        String.format("Account balance with account id = %s and currency = %s not exists in system",
                                accountId, currency)
                ));
    }

    List<AccountBalance> findAllByAccountIdAndCurrencyIn(String accountId, Collection<Currency> currencies);

    default <T> PageDto<T> getAllByAccountIdAndCurrencyIn(String accountId, Collection<Currency> currencies,
                                                          Function<AccountBalance, T> map) {
        var balances = findAllByAccountIdAndCurrencyIn(accountId, currencies);
        return PageDto.of(balances, map);
    }

    List<AccountBalance> findAllByAccountId(String accountId);

    default <T> PageDto<T> getAllByAccountId(String accountId, Function<AccountBalance, T> map) {
        var balances = findAllByAccountId(accountId);
        return PageDto.of(balances, map);
    }

    default <T> PageDto<T> getAll(Function<AccountBalance, T> map) {
        var balances = findAll();
        return PageDto.of(balances, map);
    }

}