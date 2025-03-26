package org.jbd.JBD_MINOR1.Repository;

import jakarta.transaction.Transactional;
import org.jbd.JBD_MINOR1.Model.Txn;
import org.jbd.JBD_MINOR1.Model.TxnStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TxnRepository extends JpaRepository<Txn,Integer> {
    Txn findByUserPhoneNoAndBookBookNoAndTxnStatus(String phoneNo, String bookNo, TxnStatus status);

    @Transactional
    @Modifying
    @Query(value = "update txn set created_on='2025-02-5 19:12:15.228000' ,settlement_amount=0,txn_status=0 where id=1",nativeQuery = true)
    void updateExistingTxn();

    @Transactional
    @Modifying
    @Query(value = "update book set user_id=12 where id=1",nativeQuery = true)
    void updateBook();
}
