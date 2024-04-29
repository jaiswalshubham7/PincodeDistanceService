package com.spyj.pincodedistance.Dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ExceptionDto {
    private String message;
    private String status;
    private String error;
    private String timestamp;
    @JsonIgnore
    private String trace;
}
