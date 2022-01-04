package io.cryptobrewmaster.ms.be.account.balance.db.jpa.entity.balance.blocked;

import io.cryptobrewmaster.ms.be.account.balance.constants.Operation;
import io.cryptobrewmaster.ms.be.library.constants.Currency;
import io.cryptobrewmaster.ms.be.library.constants.account.balance.BlockedBalanceStatus;
import io.cryptobrewmaster.ms.be.library.constants.audit.AuditAction;
import io.cryptobrewmaster.ms.be.library.constants.audit.AuditSource;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AccountBlockedBalanceHistory.class)
public abstract class AccountBlockedBalanceHistory_ extends io.cryptobrewmaster.ms.be.account.balance.db.jpa.entity.AbstractEntity_ {

	public static volatile SingularAttribute<AccountBlockedBalanceHistory, String> accountId;
	public static volatile SingularAttribute<AccountBlockedBalanceHistory, BigDecimal> oldQuantity;
	public static volatile SingularAttribute<AccountBlockedBalanceHistory, BigDecimal> blockedQuantity;
	public static volatile SingularAttribute<AccountBlockedBalanceHistory, AuditAction> action;
	public static volatile SingularAttribute<AccountBlockedBalanceHistory, Currency> currency;
	public static volatile SingularAttribute<AccountBlockedBalanceHistory, Long> id;
	public static volatile SingularAttribute<AccountBlockedBalanceHistory, AuditSource> source;
	public static volatile SingularAttribute<AccountBlockedBalanceHistory, Operation> operation;
	public static volatile SingularAttribute<AccountBlockedBalanceHistory, BlockedBalanceStatus> status;

	public static final String ACCOUNT_ID = "accountId";
	public static final String OLD_QUANTITY = "oldQuantity";
	public static final String BLOCKED_QUANTITY = "blockedQuantity";
	public static final String ACTION = "action";
	public static final String CURRENCY = "currency";
	public static final String ID = "id";
	public static final String SOURCE = "source";
	public static final String OPERATION = "operation";
	public static final String STATUS = "status";

}

