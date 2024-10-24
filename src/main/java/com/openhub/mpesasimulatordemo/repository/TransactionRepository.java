package com.openhub.mpesasimulatordemo.repository;

import com.openhub.mpesasimulatordemo.entities.TransactionMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionMessage, Long> {
}
