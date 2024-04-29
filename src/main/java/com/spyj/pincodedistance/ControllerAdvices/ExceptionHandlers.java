package com.spyj.pincodedistance.ControllerAdvices;

import com.spyj.pincodedistance.ControllerAdvices.Exceptions.MapsAPIException;
import com.spyj.pincodedistance.Dtos.ExceptionDto;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.Arrays;
import java.util.logging.Logger;

@ControllerAdvice
public class ExceptionHandlers {
    @ExceptionHandler(MapsAPIException.class)
    public ResponseEntity<ExceptionDto> handleMapsAPIException(MapsAPIException mapsAPIException) {
        ExceptionDto exceptionDto = new ExceptionDto(
                mapsAPIException.getMessage(),
                "503",
                "Service Unavailable",
                Instant.now().toString(),
                Arrays.toString(mapsAPIException.getStackTrace())
        );
        Logger.getLogger("Trace : " + exceptionDto.getTrace());
        return new ResponseEntity<>(
                exceptionDto, HttpStatusCode.valueOf(503)
        );
    }
}


