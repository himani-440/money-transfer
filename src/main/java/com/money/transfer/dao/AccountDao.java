package com.money.transfer.dao;

import static com.money.transfer.db.tables.Account.ACCOUNT;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.impl.DSL;

import com.money.transfer.exception.AccountServiceException;

public class AccountDao {

	public final DSLContext ctx;
	String errorCode = null;
	String errorMsg = null;

	public AccountDao(DSLContext ctx) {
		this.ctx = ctx;
	}

	public List<com.money.transfer.domain.Account> getAccountByAccNum(String accNumber) {
		return ctx.select(ACCOUNT.ID, ACCOUNT.BALANCE, ACCOUNT.NUMBER, ACCOUNT.STATUS).from(ACCOUNT)
				.where(ACCOUNT.NUMBER.equalIgnoreCase(accNumber)).fetch().into(com.money.transfer.domain.Account.class);
	}

	public List<com.money.transfer.domain.Account> getAccountBalance(String accNumber) {
		return ctx.select(ACCOUNT.BALANCE).from(ACCOUNT).where(ACCOUNT.NUMBER.equalIgnoreCase(accNumber)).fetch()
				.into(com.money.transfer.domain.Account.class);
	}

	public void creditMoney(String accNumber, Double amount) {
		try {
			ctx.transaction(ctx -> {
				final Record rec = DSL.using(ctx).select(ACCOUNT.BALANCE).from(ACCOUNT)
						.where(ACCOUNT.NUMBER.equalIgnoreCase(accNumber)).forUpdate().of(ACCOUNT.BALANCE).fetch()
						.get(0);

				final Double currBalance = rec.getValue(ACCOUNT.BALANCE);
				final Double updatedBalance = currBalance + amount;

				DSL.using(ctx).update(ACCOUNT).set(ACCOUNT.BALANCE, updatedBalance)
						.set(ACCOUNT.MODIFIED_TIME, new Timestamp(new Date().getTime()))
						.where(ACCOUNT.NUMBER.equalIgnoreCase(accNumber)).execute();
			});

		} catch (final Exception e) {
			throw new AccountServiceException("500", "Something went wrong.Please try after some time");
		}

	}

	public void debitMoney(String accNumber, Double amount) {
		try {
			ctx.transaction(ctx -> {
				final Record rec = DSL.using(ctx).select(ACCOUNT.BALANCE).from(ACCOUNT)
						.where(ACCOUNT.NUMBER.equalIgnoreCase(accNumber)).forUpdate().of(ACCOUNT.BALANCE).fetch()
						.get(0);

				final Double currBalance = rec.getValue(ACCOUNT.BALANCE);
				if (currBalance >= amount) {
					final Double updatedBalance = currBalance - amount;
					DSL.using(ctx).update(ACCOUNT).set(ACCOUNT.BALANCE, updatedBalance)
							.set(ACCOUNT.MODIFIED_TIME, new Timestamp(new Date().getTime()))
							.where(ACCOUNT.NUMBER.equalIgnoreCase(accNumber)).execute();
				} else {
					errorCode = "400";
					errorMsg = "Insufficient Money";
					throw new AccountServiceException(errorCode, errorMsg);
				}
			});
		} catch (final Exception e) {
			throw new AccountServiceException(errorCode != null ? errorCode : "500",
					errorMsg != null ? errorMsg : "Something went wrong.Please try after some time");
		}
	}

	public void transferMoney(String fromAccount, String toAccount, Double amount) throws Exception {
		try {
			ctx.transaction(ctx -> {
				final Record rec = DSL.using(ctx).select(ACCOUNT.BALANCE).from(ACCOUNT)
						.where(ACCOUNT.NUMBER.equalIgnoreCase(fromAccount)).forUpdate().of(ACCOUNT.BALANCE).fetch()
						.get(0);

				final Double currBalanceFromAcc = rec.getValue(ACCOUNT.BALANCE);

				if (currBalanceFromAcc >= amount) {
					final Double updatedBalanceFromAcc = currBalanceFromAcc - amount;
					DSL.using(ctx).update(ACCOUNT).set(ACCOUNT.BALANCE, updatedBalanceFromAcc)
							.set(ACCOUNT.MODIFIED_TIME, new Timestamp(new Date().getTime()))
							.where(ACCOUNT.NUMBER.equalIgnoreCase(fromAccount)).execute();

					final Record record = DSL.using(ctx).select(ACCOUNT.BALANCE).from(ACCOUNT)
							.where(ACCOUNT.NUMBER.equalIgnoreCase(toAccount)).forUpdate().of(ACCOUNT.BALANCE).fetch()
							.get(0);
					final Double currBalanceToAcc = record.getValue(ACCOUNT.BALANCE);
					final Double updatedBalanceToAcc = currBalanceToAcc + amount;
					DSL.using(ctx).update(ACCOUNT).set(ACCOUNT.BALANCE, updatedBalanceToAcc)
							.set(ACCOUNT.MODIFIED_TIME, new Timestamp(new Date().getTime()))
							.where(ACCOUNT.NUMBER.equalIgnoreCase(toAccount)).execute();
				} else {
					errorCode = "400";
					errorMsg = "Insufficient Money";
					throw new AccountServiceException(errorCode, errorMsg);
				}
			});
		} catch (final Exception e) {
			throw new AccountServiceException(errorCode != null ? errorCode : "500",
					errorMsg != null ? errorMsg : "Something went wrong.Please try after some time");
		}
	}
}
