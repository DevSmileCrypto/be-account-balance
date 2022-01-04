package io.cryptobrewmaster.ms.be.account.balance.db.mongodb.repository;

import io.cryptobrewmaster.ms.be.account.balance.db.mongodb.document.AccountBalanceOperationTransactionHistory;
import io.cryptobrewmaster.ms.be.library.constants.account.balance.BalanceOperationTransactionStatus;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;

public interface AccountBalanceOperationTransactionHistoryRepository extends MongoRepository<AccountBalanceOperationTransactionHistory, ObjectId> {

    boolean existsByAccountIdAndRequestIdAndOperationIdAndOperationTransactionStatusIn(String accountId, String requestId, String operationId,
                                                                                       Collection<BalanceOperationTransactionStatus> statuses);

}
