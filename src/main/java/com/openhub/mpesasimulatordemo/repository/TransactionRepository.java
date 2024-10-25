package com.openhub.mpesasimulatordemo.repository;

import com.openhub.mpesasimulatordemo.entities.TransactionMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<TransactionMessage, Long> {
    @Query("select transactionMessage from TransactionMessage transactionMessage where transactionMessage.merchantRequestID = :merchantRequestID")
    Optional<TransactionMessage> findByMerchantRequestID(String merchantRequestID);
}
