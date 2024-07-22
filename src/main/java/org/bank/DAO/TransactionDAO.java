package org.bank.DAO;

import java.sql.Date;
import java.util.List;

import org.bank.entity.Transaction;

public interface TransactionDAO {
	Transaction saveTransaction(Transaction transaction);
	List<Transaction> findbyIdandTransactionDetails(int id,Date from,Date to);
}
