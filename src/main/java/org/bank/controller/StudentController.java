package org.bank.controller;

import java.sql.Date;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

import org.bank.DAO.CustomerDAO;
import org.bank.DAO.TransactionDAO;
import org.bank.entity.Customer;
import org.bank.entity.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/home")
public class StudentController {

	@Autowired
	CustomerDAO customerDAO;
	
	@Autowired
	TransactionDAO transactionDAO;
	
	@RequestMapping("/registration")
	public String registration() {
		return "registration";
	}
	
	@RequestMapping(value = "/registrationpage", method = RequestMethod.POST)
	public String registrationPage(Customer customerDetails) {
		Random random = new Random();
		long accountNo = random.nextInt(1000000000);
		if (accountNo<1000000000) {
			accountNo+=1000000000;
		}
		String actNo = Long.toString(accountNo)+"0";
		customerDetails.setAccountnumber(actNo);
		Customer customer = customerDAO.registration(customerDetails);
		if (customer.getId()!=null) {
			return "login";
		} else {
			return "registration";
		}
	}

	@RequestMapping("/login")
	public String customerLoginPage() {
		return "login";
	}
	
	@PostMapping("/loginpage")
	public String customerlogin(@RequestParam("mailormob")String emailidOrMobile,@RequestParam("pass")String password,Model model,HttpServletRequest req) {
		HttpSession httpSession = req.getSession();
		Customer info = customerDAO.customerLogin(emailidOrMobile, password);
		if (info!=null) {
			httpSession.setAttribute("loginInfoAct", info.getAccountnumber());
			httpSession.setAttribute("loginInfoid", info.getId());
			httpSession.setAttribute("loginInfoEmail", info.getEmailid());
			return "HomePage";
		}
		else{
			model.addAttribute("msg","Invalid Details");
			return "login";
		}
	}
	
	@RequestMapping("/forgotpassword")
	public String forgotpasswordpage() {
		return "forgotpassword";
	}
	
	@RequestMapping(value = "/update")
	public String updatePassword(String emp,Date dob,String newpassword,String confirmpassword,Model model) {
		if (newpassword.equals(confirmpassword)) {
			Customer updation = customerDAO.passwordUpdation(emp, dob);
			if (updation!=null) {
				updation.setPassword(newpassword);
				Customer cust = customerDAO.registration(updation);
				return "login";
			} else {
				model.addAttribute("msg","Invalid Details");
				return "forgotpassword";
			}
		} else {
			model.addAttribute("msg","Invalid Password");
			return "forgotpassword";
		}
	}
	
	@RequestMapping("/homee")
	public String h1() {
		return "HomePage";
	}
	
	@RequestMapping("/checkbalance")
	public String checkbalance(Model model,HttpServletRequest req) {
		HttpSession session = req.getSession();
		Random random = new Random();
		int otp = random.nextInt(10000);
		if (otp<1000) {
			otp+=1000;
		}
		model.addAttribute("otp", otp);
		session.setAttribute("sesOTP", otp);
		return "checkingbalance";
	}
	
	@RequestMapping("/userchecking")
	public String fetchingDataforcheckbalance(int userotp,String userpassword,HttpServletRequest req,Model model) {
		HttpSession session = req.getSession();
		int otp = (int)session.getAttribute("sesOTP");
		String emaill = (String)session.getAttribute("loginInfoEmail");
		if (otp == userotp) {
			Customer customer = customerDAO.getDetailsByEmailIdAndPassword(emaill, userpassword);
			if (customer!=null) {
				model.addAttribute("c1d", "Customer Details");
				model.addAttribute("c1act", "Account Number :    "+customer.getAccountnumber());
				model.addAttribute("c1n", "Customer Name :     "+customer.getFirstname()+" "+customer.getLastname());
				model.addAttribute("c1amount", "Balance :    "+customer.getAmount());
				return "userinfo";
			} else {
				Random random = new Random();
				otp = random.nextInt(10000);
				if (otp<1000) {
					otp+=1000;
				}
				model.addAttribute("otp", otp);
				session.setAttribute("sesOTP", otp);
				model.addAttribute("mssg", "Invalid Password");
				return "checkingbalance";
			}
		} else {
			Random random = new Random();
			otp = random.nextInt(10000);
			if (otp<1000) {
				otp+=1000;
			}
			model.addAttribute("otp", otp);
			session.setAttribute("sesOTP", otp);
			model.addAttribute("mssg", "Enter valid OTP");
			return "checkingbalance";
		}
	}

	@RequestMapping("/debit")
	public String debit() {
		return "debitamount";
	}
	
	@RequestMapping("/transaction1")
	public String transaction1(String accountnumber,String amount,Model model,HttpServletRequest req) {
		HttpSession httpSession = req.getSession();
		String senderActNo = (String)httpSession.getAttribute("loginInfoAct");
		Customer senderCustomer = customerDAO.getDetailsByAccoutnumber(senderActNo);
		double senderAmount = senderCustomer.getAmount();
		double inputamount = Double.parseDouble(amount);
		Customer receiverCustomer = customerDAO.getDetailsByAccoutnumber(accountnumber);
		if (receiverCustomer!=null) {
			if (senderAmount >= inputamount) {
				senderCustomer.setAmount(senderAmount-inputamount);
				Customer customer1 = customerDAO.registration(senderCustomer);
				receiverCustomer.setAmount(receiverCustomer.getAmount()+inputamount);
				Customer customer2 = customerDAO.registration(receiverCustomer);
				
				Transaction t = new Transaction();
				t.setCustomerid(senderCustomer.getId());
				t.setTransactionamount(inputamount);
				t.setTransactiondate(Date.valueOf(LocalDate.now()));
				t.setTransactiontime(String.valueOf(Time.valueOf(LocalTime.now())));
				t.setTransactiontype("Debit");
				
				Transaction transaction = transactionDAO.saveTransaction(t);
				model.addAttribute("c1act", "Amount Successfully Transferred");
				return "userinfo";
			} else {
				model.addAttribute("c1act", "Invalid Amount");
				return "userinfo";
			}
		} else {
			model.addAttribute("c1act", "Invalid Account Number");
			return "userinfo";
		}
	}
	
	@RequestMapping("/credit")
	public String credit() {
		return "creditamount";
	}
	
	@RequestMapping("/transaction2")
	public String transaction2(String acctNo,String amount,Model model,HttpServletRequest req) {
		HttpSession httpSession = req.getSession();
		int id = (int)httpSession.getAttribute("loginInfoid");
		double inputamount = Double.parseDouble(amount);
		Customer customer = customerDAO.getDetailsByAccoutnumber(acctNo);
		if (customer!=null) {
			if (inputamount > 0) {
				customer.setAmount(customer.getAmount()+inputamount);
				Customer cust = customerDAO.registration(customer);
				
				Transaction t = new Transaction();
				t.setCustomerid(id);
				t.setTransactionamount(inputamount);
				t.setTransactiondate(Date.valueOf(LocalDate.now()));
				t.setTransactiontime(String.valueOf(Time.valueOf(LocalTime.now())));
				t.setTransactiontype("Credit");
				
				Transaction transaction = transactionDAO.saveTransaction(t);
				model.addAttribute("c1act", "Amount Successfully Credited");
				return "userinfo";
			} else {
				model.addAttribute("c1act", "Invalid Amount");
				return "userinfo";
			}
		} else {
			model.addAttribute("c1act", "Invalid Account Number");
			return "userinfo";
		}
	}
	
	
	@RequestMapping("/statement")
	public String statement() {
		return "Statementpage";
	}
	
	@RequestMapping(value = "/statementcheck",method = RequestMethod.GET)
	public String statementcheck(Date fromdate,Date todate,HttpServletRequest req,Model model) {
		HttpSession httpSession = req.getSession();
		int id = (int)httpSession.getAttribute("loginInfoid");
		List<Transaction> list = transactionDAO.findbyIdandTransactionDetails(id, fromdate, todate);
		if (list.isEmpty()) {
			model.addAttribute("msgg", "No Statements Found");
			return "statement";
		} else {
			model.addAttribute("listofstatements", list);
			return "statement";
		}
		
	}
}
