package io.cryptobrewmaster.ms.be.account.balance.db.entity.blocked;

import io.cryptobrewmaster.ms.be.account.balance.constants.BalanceOperation;
import io.cryptobrewmaster.ms.be.account.balance.db.entity.AccountBalance;
import io.cryptobrewmaster.ms.be.library.constants.account.balance.BalanceChangeAction;
import io.cryptobrewmaster.ms.be.library.constants.account.balance.BalanceChangeSource;
import io.cryptobrewmaster.ms.be.library.constants.account.balance.BalanceChangeStatus;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AccountBlockedBalance.class)
public abstract class AccountBlockedBalance_ {

	public static volatile SingularAttribute<AccountBlockedBalance, BigDecimal> oldQuantity;
	public static volatile SingularAttribute<AccountBlockedBalance, Long> createdDate;
	public static volatile SingularAttribute<AccountBlockedBalance, Long> lastModifiedDate;
	public static volatile SingularAttribute<AccountBlockedBalance, BigDecimal> blockedQuantity;
	public static volatile SingularAttribute<AccountBlockedBalance, BalanceChangeAction> action;
	public static volatile SingularAttribute<AccountBlockedBalance, Long> id;
	public static volatile SingularAttribute<AccountBlockedBalance, BalanceChangeSource> source;
	public static volatile SingularAttribute<AccountBlockedBalance, AccountBalance> accountBalance;
	public static volatile SingularAttribute<AccountBlockedBalance, BalanceOperation> operation;
	public static volatile SingularAttribute<AccountBlockedBalance, BalanceChangeStatus> status;

	public static final String OLD_QUANTITY = "oldQuantity";
	public static final String CREATED_DATE = "createdDate";
	public static final String LAST_MODIFIED_DATE = "lastModifiedDate";
	public static final String BLOCKED_QUANTITY = "blockedQuantity";
	public static final String ACTION = "action";
	public static final String ID = "id";
	public static final String SOURCE = "source";
	public static final String ACCOUNT_BALANCE = "accountBalance";
	public static final String OPERATION = "operation";
	public static final String STATUS = "status";

}

