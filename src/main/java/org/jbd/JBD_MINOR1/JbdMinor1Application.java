package org.jbd.JBD_MINOR1;

import org.jbd.JBD_MINOR1.Repository.BookRepository;
import org.jbd.JBD_MINOR1.Repository.TxnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JbdMinor1Application implements CommandLineRunner {

	@Autowired
	private TxnRepository txnRepository;

	public static void main(String[] args) {
		SpringApplication.run(JbdMinor1Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		txnRepository.updateExistingTxn();
		txnRepository.updateBook();
		System.out.println("My application has started..!!");
	}
}
