package io.cryptobrewmaster.ms.be.account.balance.db.repository;

import io.cryptobrewmaster.ms.be.account.balance.db.entity.AccountBalance;
import io.cryptobrewmaster.ms.be.account.balance.db.entity.AccountBalance_;
import io.cryptobrewmaster.ms.be.account.balance.web.model.criteria.AccountBalanceFetchedCriteriaDto;
import io.cryptobrewmaster.ms.be.library.constants.Currency;
import io.cryptobrewmaster.ms.be.library.exception.ParametersAbsentOrInvalidException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface AccountBalanceRepository extends JpaRepository<AccountBalance, Long>, JpaSpecificationExecutor<AccountBalance> {

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

    default Page<AccountBalance> fetchByCriteria(AccountBalanceFetchedCriteriaDto criteria) {
        var specification = filter(criteria);
        var sort = sort(criteria);
        var pageable = pagination(criteria, sort);
        return findAll(specification, pageable);
    }

    default <T> Page<T> fetchByCriteria(AccountBalanceFetchedCriteriaDto criteria, Function<AccountBalance, T> map) {
        var page = fetchByCriteria(criteria);
        var elements = page.getContent().stream()
                .map(map)
                .collect(Collectors.toList());
        return new PageImpl<>(elements, page.getPageable(), page.getTotalElements());
    }

    private Specification<AccountBalance> filter(AccountBalanceFetchedCriteriaDto criteria) {
        var specification = Specification.<AccountBalance>where(null);

        if (criteria.hasAccountId()) {
            specification = specification.and(fetchByAccountId(criteria.getAccountId()));
        }
        if (criteria.hasCurrencies()) {
            specification = specification.and(fetchByCurrencies(criteria.getCurrencies()));
        }
        return specification;
    }

    private Sort sort(AccountBalanceFetchedCriteriaDto criteria) {
        return Sort.sort(AccountBalance.class);
    }

    private Pageable pagination(AccountBalanceFetchedCriteriaDto criteria, Sort sort) {
        return Pageable.unpaged();
    }

    private Specification<AccountBalance> fetchByAccountId(String accountId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(AccountBalance_.accountId), accountId);
    }

    private Specification<AccountBalance> fetchByCurrencies(Collection<Currency> currencies) {
        return (root, query, criteriaBuilder) -> {
            CriteriaBuilder.In<Currency> in = criteriaBuilder.in(root.get(AccountBalance_.currency));
            currencies.forEach(in::value);
            return in;
        };
    }

}