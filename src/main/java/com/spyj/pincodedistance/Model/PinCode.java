package com.spyj.pincodedistance.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PinCode extends BaseModel implements Serializable {
    private String pinCode;
    private String lat;
    private String lon;
}
