package com.spyj.pincodedistance.Dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RouteRequest {
    private String fromPinCode;
    private String toPinCode;
}
