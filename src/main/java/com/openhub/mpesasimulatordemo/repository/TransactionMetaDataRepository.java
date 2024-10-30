package com.openhub.mpesasimulatordemo.repository;

import com.openhub.mpesasimulatordemo.entities.TransactionMetaData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TransactionMetaDataRepository extends JpaRepository<TransactionMetaData, Long> {
    @Query("SELECT t FROM TransactionMetaData t WHERE t.mpesaReceiptNumber= :transactionId")
    Optional<TransactionMetaData> findByTransactionId(String transactionId);
}
