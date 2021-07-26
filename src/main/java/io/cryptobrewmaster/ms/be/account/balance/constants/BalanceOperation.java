package io.cryptobrewmaster.ms.be.account.balance.constants;

import io.cryptobrewmaster.ms.be.account.balance.db.entity.balance.AccountBalance;
import io.cryptobrewmaster.ms.be.account.balance.db.entity.balance.blocked.AccountBalanceBlocked;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.function.BiConsumer;

@Getter
@RequiredArgsConstructor
public enum BalanceOperation {
    ADD((accountBalance, accountBlockedBalance) -> {
        var newQuantity = accountBalance.getQuantity().subtract(accountBlockedBalance.getBlockedQuantity());
        newQuantity = newQuantity.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : newQuantity;
        accountBalance.setQuantity(newQuantity);
    }),
    SUBTRACT((accountBalance, accountBlockedBalance) -> {
        var newQuantity = accountBalance.getQuantity().add(accountBlockedBalance.getBlockedQuantity());
        accountBalance.setQuantity(newQuantity);
    });

    private final BiConsumer<AccountBalance, AccountBalanceBlocked> rollbackOperationBiConsumer;

}
