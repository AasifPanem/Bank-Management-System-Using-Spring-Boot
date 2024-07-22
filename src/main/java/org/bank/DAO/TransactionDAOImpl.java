package org.bank.DAO;

import java.sql.Date;
import java.util.List;

import org.bank.entity.Transaction;
import org.bank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionDAOImpl implements TransactionDAO{

	@Autowired
	TransactionRepository transactionRepository;
	
	@Override
	public Transaction saveTransaction(Transaction transaction) {
		return transactionRepository.save(transaction);
	}

	@Override
	public List<Transaction> findbyIdandTransactionDetails(int id, Date from, Date to) {
		return transactionRepository.findByCustomeridAndTransactiondateGreaterThanEqualAndTransactiondateLessThanEqual(id, from, to);
	}

}
