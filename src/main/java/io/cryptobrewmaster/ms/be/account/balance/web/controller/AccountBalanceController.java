package io.cryptobrewmaster.ms.be.account.balance.web.controller;

import io.cryptobrewmaster.ms.be.account.balance.service.AccountBalanceService;
import io.cryptobrewmaster.ms.be.account.balance.web.model.AccountBalanceDto;
import io.cryptobrewmaster.ms.be.account.balance.web.model.param.AccountBalanceRequestParam;
import io.cryptobrewmaster.ms.be.account.balance.web.model.request.AccountBalanceChangedRequestDto;
import io.cryptobrewmaster.ms.be.account.balance.web.model.response.AccountBalanceChangedResponseDto;
import io.cryptobrewmaster.ms.be.library.constants.Currency;
import io.cryptobrewmaster.ms.be.library.dto.PageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/account/balance")
@RestController
public class AccountBalanceController {

    private final AccountBalanceService accountBalanceService;

    @GetMapping("/{currency}")
    public AccountBalanceDto getByAccountIdAndCurrency(@Valid @NotNull @PathVariable Currency currency,
                                                       @Valid @NotNull @RequestParam String accountId) {
        log.info("Request to get balance by account id and currency received. Account id = {}, currency = {}", accountId, currency);
        var balanceDto = accountBalanceService.getByAccountIdAndCurrency(accountId, currency);
        log.info("Response on get balance by account id and currency. {}", balanceDto);
        return balanceDto;
    }

    @GetMapping
    public PageDto<AccountBalanceDto> getAll(@Valid @NotNull AccountBalanceRequestParam param) {
        log.info("Request to get all balances by request param received. {}", param);
        var balancePageDto = accountBalanceService.getAll(param);
        log.info("Response on get all balances by request param. {}", balancePageDto);
        return balancePageDto;
    }

    @PutMapping("/add")
    public AccountBalanceChangedResponseDto add(@Valid @NotNull @RequestBody AccountBalanceChangedRequestDto accountBalanceChangedRequestDto) {
        log.info("Request to add quantity to balance received. {}", accountBalanceChangedRequestDto);
        var accountBalanceChangedResponseDto = accountBalanceService.add(accountBalanceChangedRequestDto);
        log.info("Response on add quantity to balance. {}", accountBalanceChangedResponseDto);
        return accountBalanceChangedResponseDto;
    }

    @PutMapping("/subtract")
    public AccountBalanceChangedResponseDto subtract(@Valid @NotNull @RequestBody AccountBalanceChangedRequestDto accountBalanceChangedRequestDto) {
        log.info("Request to subtract quantity to balance received. {}", accountBalanceChangedRequestDto);
        var accountBalanceChangedResponseDto = accountBalanceService.subtract(accountBalanceChangedRequestDto);
        log.info("Response on subtract quantity to balance. {}", accountBalanceChangedResponseDto);
        return accountBalanceChangedResponseDto;
    }

}
