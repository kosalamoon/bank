package com.scbb.bank.report.service;

import com.scbb.bank.authentication.security.JwtTokenProvider;
import com.scbb.bank.ledger.model.Entry;
import com.scbb.bank.ledger.model.Transaction;
import com.scbb.bank.ledger.model.enums.OperationType;
import com.scbb.bank.ledger.service.TransactionService;
import com.scbb.bank.person.model.User;
import com.scbb.bank.person.service.UserService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class CashierReportService {

	private TransactionService transactionService;
	private JwtTokenProvider tokenProvider;
	private UserService userService;

	public CashierReportService(TransactionService transactionService, JwtTokenProvider tokenProvider, UserService userService) {
		this.transactionService = transactionService;
		this.tokenProvider = tokenProvider;
		this.userService = userService;
	}


	public void deposit(Integer id, String token, String operationType, HttpServletResponse response) {
		String substring = token.substring(7);
		System.out.println(substring);
		User user = userService.findById(tokenProvider.getUserIdFromToken(substring));
		try {

			InputStream stream = this.getClass().getResourceAsStream("/reports/voucher.jrxml");
			JasperDesign jasperDesign = JRXmlLoader.load(stream);
			JasperReport report = JasperCompileManager.compileReport(jasperDesign);

			Map<String, Object> map = new HashMap<>();
			Transaction transaction = transactionService.findById(id);
			String member = "";
			BigDecimal balance = new BigDecimal("0");
			String accountNumber = "";
			for (Entry entry : transaction.getEntryList()) {
				if (entry.getOperationType() == OperationType.Credit) {
					member = entry.getAccount().getSavings().getMember().getFullName();
					balance = entry.getAccount().getBalance();
					accountNumber = entry.getAccount().getNumber();
				}
			}
			LocalDate date = transaction.getDateTime().toLocalDate();
			LocalTime time = transaction.getDateTime().toLocalTime();
			String transactionDateTime = date + " @ " + String.valueOf(time.getHour()) + ":" + String.valueOf(time.getSecond());

			map.put("generatedUser", user.getUsername());
			map.put("transactionDateTime", transactionDateTime);
			map.put("transactionUser", transaction.getUser().getUsername());
			map.put("member", member);
			map.put("balance", balance);
			map.put("amount", transaction.getEntryList().get(0).getAmount());
			map.put("transId", id);
			map.put("accountNumber", accountNumber);
			if (operationType.equals("deposit"))
				map.put("operationType", "Deposit");
			else
				map.put("operationType", "Withdraw");

			JasperPrint jasperPrint = JasperFillManager.fillReport(report, map, new JREmptyDataSource(1));
			response.setContentType("application/x-pdf");
			response.setHeader("Content-Disposition", "inline; filename=voucher.pdf");

			OutputStream outputStream = response.getOutputStream();
			JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

		} catch (JRException | IOException e) {
			e.printStackTrace();
		}

	}
}