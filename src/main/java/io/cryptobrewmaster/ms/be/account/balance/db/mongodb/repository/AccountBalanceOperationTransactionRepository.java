package io.cryptobrewmaster.ms.be.account.balance.db.mongodb.repository;

import io.cryptobrewmaster.ms.be.account.balance.db.mongodb.document.AccountBalanceOperationTransaction;
import io.cryptobrewmaster.ms.be.library.constants.account.balance.BalanceOperationTransactionStatus;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface AccountBalanceOperationTransactionRepository extends MongoRepository<AccountBalanceOperationTransaction, ObjectId> {

    Optional<AccountBalanceOperationTransaction> findByAccountIdAndRequestIdAndOperationIdAndOperationTransactionStatusIn(String accountId, String requestId, String operationId,
                                                                                                                          Collection<BalanceOperationTransactionStatus> statuses);

    Page<AccountBalanceOperationTransaction> findAllByOperationTransactionStatus(BalanceOperationTransactionStatus status,
                                                                                 Pageable pageable);

    default List<AccountBalanceOperationTransaction> findAllByOperationTransactionStatus(BalanceOperationTransactionStatus status,
                                                                                         Integer page, Integer size) {
        return findAllByOperationTransactionStatus(status, PageRequest.of(page, size)).getContent();
    }

}
