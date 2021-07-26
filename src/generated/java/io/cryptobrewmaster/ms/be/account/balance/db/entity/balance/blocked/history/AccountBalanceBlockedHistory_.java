package io.cryptobrewmaster.ms.be.account.balance.db.entity.balance.blocked.history;

import io.cryptobrewmaster.ms.be.account.balance.constants.BalanceOperation;
import io.cryptobrewmaster.ms.be.account.balance.db.entity.balance.AccountBalance;
import io.cryptobrewmaster.ms.be.library.constants.account.balance.BalanceChangeStatus;
import io.cryptobrewmaster.ms.be.library.constants.audit.AuditAction;
import io.cryptobrewmaster.ms.be.library.constants.audit.AuditSource;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AccountBalanceBlockedHistory.class)
public abstract class AccountBalanceBlockedHistory_ extends io.cryptobrewmaster.ms.be.account.balance.db.entity.AbstractEntity_ {

	public static volatile SingularAttribute<AccountBalanceBlockedHistory, BigDecimal> oldQuantity;
	public static volatile SingularAttribute<AccountBalanceBlockedHistory, BigDecimal> blockedQuantity;
	public static volatile SingularAttribute<AccountBalanceBlockedHistory, AuditAction> action;
	public static volatile SingularAttribute<AccountBalanceBlockedHistory, Long> id;
	public static volatile SingularAttribute<AccountBalanceBlockedHistory, AuditSource> source;
	public static volatile SingularAttribute<AccountBalanceBlockedHistory, AccountBalance> accountBalance;
	public static volatile SingularAttribute<AccountBalanceBlockedHistory, BalanceOperation> operation;
	public static volatile SingularAttribute<AccountBalanceBlockedHistory, BalanceChangeStatus> status;

	public static final String OLD_QUANTITY = "oldQuantity";
	public static final String BLOCKED_QUANTITY = "blockedQuantity";
	public static final String ACTION = "action";
	public static final String ID = "id";
	public static final String SOURCE = "source";
	public static final String ACCOUNT_BALANCE = "accountBalance";
	public static final String OPERATION = "operation";
	public static final String STATUS = "status";

}

