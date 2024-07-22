package org.bank.repository;

import java.sql.Date;
import java.util.List;

import org.bank.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Integer>{
	List<Transaction> findByCustomeridAndTransactiondateGreaterThanEqualAndTransactiondateLessThanEqual(int id,Date from,Date to);
}
