package org.jbd.JBD_MINOR1.Service;

import jakarta.transaction.Transactional;
import lombok.Setter;
import org.jbd.JBD_MINOR1.Exception.TxnException;
import org.jbd.JBD_MINOR1.Model.*;
import org.jbd.JBD_MINOR1.Repository.TxnRepository;
import org.jbd.JBD_MINOR1.Repository.UserRepository;
import org.jbd.JBD_MINOR1.dto.TxnRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class TxnService {

    //It is not correct way to do it as it is the job of userservice to get data fom user repository.
//    @Autowired
//    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private TxnRepository txnRepository;

    @Value("${book.valid.upto}")
    private String validDays;


    //Could have taken in string then in that case in line 144....
    @Value("${book.fine.amount.per.day}")
    private String finePerDay;

    //These parts are need not to be transactional so these are outside kept in a separate method
    public User getUserFromDB(TxnRequest txnRequest) throws TxnException {
        //student is correct?
        User userFromDb = userService.getStudentByPhoneNo((txnRequest.getUserPhoneNo()));
        if(userFromDb == null){
            throw new TxnException("Student doesnt belong to my Library");
        }
        return userFromDb;
    }

    public Book getBookFromDb(TxnRequest txnRequest) throws TxnException {
        List<Book>  books  = bookService.filter(BookFilterType.BOOK_NO, Operator.EQUALS,txnRequest.getBookNo());
        if(books.isEmpty()){
            new TxnException("Books  doesnt belong to my Librabry");
        }
        Book bookFromDb = books.get(0);

//.....        if(bookFromDb.getUser() != null){
//            throw new TxnException("Books already has been issued to someone else ");
//        }
        return bookFromDb;
    }

    //It is only needed for this method
    @Transactional(rollbackOn = {TxnException.class})
    public String createTxn(User userFromDb, Book bookFromDb){
        String txnId = UUID.randomUUID().toString();
        Txn txn =Txn.builder().
                txnId(txnId).
                user(userFromDb).
                book(bookFromDb).
                txnStatus(TxnStatus.ISSUED).
                build();
        txnRepository.save(txn);

        bookFromDb.setUser(userFromDb);
        // updateBookData method will update the data in book table from book service.
        bookService.updateBookData(bookFromDb);

        return txnId;
    }

    // this annotation is required because we are dealing with multiple data
    // if txn is saved but the table is not updated that could be a problem
    // and we are using rollbackOn because transactional only works for runtime exception
    // TxnException is compiletime exception
    public String create(TxnRequest txnRequest) throws TxnException {

        //student is correct?
//        User userFromDb = userService.getStudentByPhoneNo((txnRequest.getUserPhoneNo()));
//        if(userFromDb == null){
//           throw new TxnException("Student doesnt belong to my Librabry");
//        }

        User userFromDb = getUserFromDB(txnRequest);
        Book bookFromDb = getBookFromDb(txnRequest);
        //.....Kept this check here coz i knw user can never be null and just to keep it
        if(bookFromDb.getUser() != null){
            throw new TxnException("Books already has been issued to someone else ");
        }
        return createTxn(userFromDb,bookFromDb);
//        List<Book>  books  = bookService.filter(BookFilterType.BOOK_NO, Operator.EQUALS,txnRequest.getBookNo());
//        if(books.isEmpty()){
//            new TxnException("Books  doesnt belong to my Librabry");
//        }
//        Book bookFromDb = books.get(0);
//
//        if(bookFromDb.getUser() != null){
//            throw new TxnException("Books already has been issued to someone else ");
//        }
        //Frontend id (different from backend id that is @Id)
//        String txnId = UUID.randomUUID().toString();
//        Txn txn =Txn.builder().
//                txnId(txnId).
//                user(userFromDb).
//                book(bookFromDb).
//                txnStatus(TxnStatus.ISSUED).
//                build();
//        txnRepository.save(txn);
//
//        bookFromDb.setUser(userFromDb);
//        // updateBookData method will update the data in book table from book service.
//        bookService.updateBookData(bookFromDb);
//
//        return txnId;
    }

    @Transactional(rollbackOn = {TxnException.class})
    public int  returnBook(TxnRequest txnRequest) throws TxnException {
        User userFromDb = getUserFromDB(txnRequest);
        Book bookFromDb = getBookFromDb(txnRequest);
        if(bookFromDb.getUser() != userFromDb){
            throw new TxnException("This is not the user which the book was assigned");
        }
        //update the table with fine amount but need to calculate the fine first
        Txn txn = txnRepository.findByUserPhoneNoAndBookBookNoAndTxnStatus(txnRequest.getUserPhoneNo(),txnRequest.getBookNo(), TxnStatus.ISSUED);
        int fine = calculateFine(txn, bookFromDb.getSecurityAmount());
        if(fine == bookFromDb.getSecurityAmount()){
             txn.setTxnStatus(TxnStatus.RETURNED);
        }else{
            txn.setTxnStatus(TxnStatus.FINED);
        }
        txn.setSettlementAmount( fine);
        bookFromDb.setUser(null);
        bookService.updateBookData(bookFromDb );
        return fine;

    }
//      Since i want to test this method i making this public or i cannot access
//      this outside this package  yet it is not the correct way
    public int calculateFine(Txn txn, int securityAmount) {
        long issueDate = txn.getCreatedOn().getTime();
        long returnDate = System.currentTimeMillis();
        long timeDiff = returnDate - issueDate;
        int daysPassed = (int) TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);
        if(daysPassed > Integer.valueOf(validDays)){
            //I need to pass Integer.valueOf for both valid days and finePerDay
            int fineAmount = (daysPassed-Integer.valueOf(validDays))*Integer.valueOf(finePerDay);
            return securityAmount-fineAmount;
        }
        return securityAmount;
    }
}
