package org.jbd.JBD_MINOR1.Service;

import org.jbd.JBD_MINOR1.Exception.TxnException;
import org.jbd.JBD_MINOR1.Model.Book;
import org.jbd.JBD_MINOR1.Model.Txn;
import org.jbd.JBD_MINOR1.Model.User;
import org.jbd.JBD_MINOR1.Repository.TxnRepository;
import org.jbd.JBD_MINOR1.dto.TxnRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestTxnService {

    @InjectMocks
    private TxnService txnService ;

    @Mock
    private UserService userService;

    @Mock
    private BookService bookService;

    @Mock
    private TxnRepository txnRepository;

    @Before
    public void setUp(){
     //   txnService = new TxnService();
//        txnService.setFinePerDay("5");
//        txnService.setValidDays("12");
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(txnService, "validDays","12");
        ReflectionTestUtils.setField(txnService, "finePerDay","2");

    }

    @Test
    public void testCalculateFine() throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2025-01-30");
        Txn txn = Txn.builder().createdOn(date).build();
        int calculatedAmount =  txnService.calculateFine(txn, 100 );
        Assert.assertEquals(64, calculatedAmount);
    }

    @Test(expected = TxnException.class)
    public void testGetUserFromDB() throws TxnException {
        TxnRequest txnRequest = TxnRequest.builder().build();
        //whenever getStudent.. method is called it will not go into another class but return null
        //as i am fixating it or mocking and an exception is expected from this method so i need
        //to pass it in @Test
        when(userService.getStudentByPhoneNo(any())).thenReturn(null);
        txnService.getUserFromDB(txnRequest);
    }

    @Test()
    public void testGetUserFromDBWithNoException() throws TxnException {
        TxnRequest txnRequest = TxnRequest.builder().build();
        User user = User.builder().id(1).build();
        when(userService.getStudentByPhoneNo(any())).thenReturn(user);//mocking
        txnService.getUserFromDB(txnRequest);
        User output = txnService.getUserFromDB(txnRequest);
        Assert.assertEquals(user.getId(), output.getId());
    }

    @Test()
    public void testReturnBook() throws TxnException, ParseException {
        TxnRequest txnRequest = TxnRequest.builder().build();
        User user = User.builder().id(1).build();
        when(userService.getStudentByPhoneNo(any())).thenReturn(user);//mocking

        List<Book> list = new ArrayList<>();
        list.add(Book.builder().id(1).bookNo("1").user(user).securityAmount(100).build());
        when(bookService.filter(any(),any(),any())).thenReturn(list);

        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2025-01-30");
        Txn txn = Txn.builder().id(1).user(user).book(list.get(0)).createdOn(date).build();
        when(txnRepository. findByUserPhoneNoAndBookBookNoAndTxnStatus(any(),any(),any())).thenReturn(txn );
        int fine = txnService.returnBook(txnRequest);
        Assert.assertEquals(64,fine);
    }
}
