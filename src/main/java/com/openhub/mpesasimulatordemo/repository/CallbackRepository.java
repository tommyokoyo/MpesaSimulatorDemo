package com.openhub.mpesasimulatordemo.repository;

import com.openhub.mpesasimulatordemo.entities.CallBackMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CallbackRepository extends JpaRepository<CallBackMessage, Long> {
}
