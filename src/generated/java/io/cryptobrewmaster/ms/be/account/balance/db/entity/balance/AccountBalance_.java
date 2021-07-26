package io.cryptobrewmaster.ms.be.account.balance.db.entity.balance;

import io.cryptobrewmaster.ms.be.library.constants.Currency;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AccountBalance.class)
public abstract class AccountBalance_ extends io.cryptobrewmaster.ms.be.account.balance.db.entity.AbstractEntity_ {

	public static volatile SingularAttribute<AccountBalance, String> accountId;
	public static volatile SingularAttribute<AccountBalance, BigDecimal> quantity;
	public static volatile SingularAttribute<AccountBalance, Currency> currency;
	public static volatile SingularAttribute<AccountBalance, Long> id;

	public static final String ACCOUNT_ID = "accountId";
	public static final String QUANTITY = "quantity";
	public static final String CURRENCY = "currency";
	public static final String ID = "id";

}

