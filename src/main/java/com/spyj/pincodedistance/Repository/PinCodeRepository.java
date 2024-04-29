package com.spyj.pincodedistance.Repository;

import com.spyj.pincodedistance.Model.PinCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PinCodeRepository extends JpaRepository<PinCode, String> {
    Optional<PinCode> findByPinCode(String pinCode);
}
