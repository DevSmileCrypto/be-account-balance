package io.cryptobrewmaster.ms.be.account.balance.kafka;

import io.cryptobrewmaster.ms.be.account.balance.communication.configuration.data.storage.dto.BalanceConfigDto;
import io.cryptobrewmaster.ms.be.account.balance.communication.configuration.data.storage.service.ConfigurationDataStorageCommunicationService;
import io.cryptobrewmaster.ms.be.account.balance.db.entity.AccountBalance;
import io.cryptobrewmaster.ms.be.account.balance.db.repository.AccountBalanceRepository;
import io.cryptobrewmaster.ms.be.library.kafka.dto.account.KafkaAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountKafkaReceiver {

    private final ConfigurationDataStorageCommunicationService configurationDataStorageCommunicationService;

    private final AccountBalanceRepository accountBalanceRepository;

    @Transactional
    public void init(KafkaAccount kafkaAccount) {
        List<BalanceConfigDto> balanceConfigDtos = configurationDataStorageCommunicationService.getAllBalanceConfig();

        List<AccountBalance> accountBalances = balanceConfigDtos.stream()
                .map(balanceConfigDto -> AccountBalance.of(kafkaAccount.getId(), balanceConfigDto))
                .collect(Collectors.toList());

        accountBalanceRepository.saveAll(accountBalances);
    }

}
