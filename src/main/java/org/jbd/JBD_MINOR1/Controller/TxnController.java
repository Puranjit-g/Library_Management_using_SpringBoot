package org.jbd.JBD_MINOR1.Controller;

import org.jbd.JBD_MINOR1.Exception.TxnException;
import org.jbd.JBD_MINOR1.Service.TxnService;
import org.jbd.JBD_MINOR1.dto.TxnRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/txn")
public class TxnController {

    @Autowired
    private TxnService txnService;

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody TxnRequest txnRequest) throws TxnException {
        String txnId =  txnService. create(txnRequest);
        return new ResponseEntity<>(txnId, HttpStatus.OK);
    }

    @PutMapping("/return")
        public ResponseEntity<Integer> returnBook(@RequestBody TxnRequest txnRequest) throws TxnException {
        int txnId = txnService.returnBook(txnRequest);
        return new ResponseEntity<>(txnId, HttpStatus.OK);
    }
}
