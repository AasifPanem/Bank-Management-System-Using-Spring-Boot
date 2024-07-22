package org.bank.DAO;

import java.sql.Date;
import java.util.Random;

import org.bank.entity.Customer;
import org.bank.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerDAOImpl implements CustomerDAO{

	@Autowired
	CustomerRepository customerRepository;
	
	@Override
	public Customer registration(Customer customerDetails) {
		return customerRepository.save(customerDetails);
	}

	@Override
	public Customer customerLogin(String emailidorpassword, String password) {
		return customerRepository.findByEmailidOrMobilenumberAndPassword(emailidorpassword, emailidorpassword, password);
	}

	@Override
	public Customer getDetailsByEmailIdAndPassword(String emailId,String password) {
		return customerRepository.readByEmailidAndPassword(emailId, password);
	}

	@Override
	public Customer getDetailsByAccoutnumber(String actNo) {
		return customerRepository.getByAccountnumber(actNo);
	}

	@Override
	public Customer passwordUpdation(String emailorpassword, Date date) {
		return customerRepository.findByEmailidOrMobilenumberAndDateofbirth(emailorpassword, emailorpassword, date);
	}

}
