package org.bank.DAO;

import java.sql.Date;

import org.bank.entity.Customer;


public interface CustomerDAO {
	Customer registration(Customer customerDetails);
	Customer customerLogin(String emailidormob,String password);
	Customer getDetailsByEmailIdAndPassword(String emailId,String password);
	Customer getDetailsByAccoutnumber(String actNo);
	Customer passwordUpdation(String emailorpassword,Date date);
}
