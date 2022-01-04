package io.cryptobrewmaster.ms.be.account.balance.web.controller;

import io.cryptobrewmaster.ms.be.account.balance.service.AccountBalanceService;
import io.cryptobrewmaster.ms.be.account.balance.web.model.AccountBalanceDto;
import io.cryptobrewmaster.ms.be.account.balance.web.model.AccountBalanceUiDto;
import io.cryptobrewmaster.ms.be.library.constants.Currency;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/account/balance")
@RestController
public class AccountBalanceController {

    private final AccountBalanceService accountBalanceService;

    @GetMapping("/{currency}")
    public AccountBalanceDto getByAccountIdAndCurrency(@Valid @NotNull @PathVariable Currency currency,
                                                       @Valid @NotBlank @RequestParam String accountId) {
        log.info("Request to get balance by account id and currency received. Account id = {}, currency = {}", accountId, currency);
        var accountBalanceDto = accountBalanceService.getByAccountIdAndCurrency(accountId, currency);
        log.info("Response on get balance by account id and currency. {}", accountBalanceDto);
        return accountBalanceDto;
    }

    @GetMapping("/{currency}/ui")
    public AccountBalanceUiDto getByAccountIdAndCurrencyForUi(@Valid @NotNull @PathVariable Currency currency,
                                                              @Valid @NotBlank @RequestParam String accountId) {
        log.info("Request to get balance by account id and currency for ui received. Account id = {}, currency = {}", accountId, currency);
        var accountBalanceUiDto = accountBalanceService.getByAccountIdAndCurrencyForUi(accountId, currency);
        log.info("Response on get balance by account id and currency for ui. {}", accountBalanceUiDto);
        return accountBalanceUiDto;
    }

}
