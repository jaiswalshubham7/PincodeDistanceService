package com.spyj.pincodedistance.Repository;

import com.spyj.pincodedistance.Model.PinCode;
import com.spyj.pincodedistance.Model.Route;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RouteRepository extends JpaRepository<Route, Long>{
    Optional<Route> findByFromPinCodeAndToPinCode(PinCode fromPinCode, PinCode toPinCode);
}
