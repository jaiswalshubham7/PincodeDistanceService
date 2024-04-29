package com.spyj.pincodedistance.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Route extends BaseModel implements Serializable{
    private String routeName;
    private double totalDistance;
    private long totalDuration;
    @OneToOne
    private PinCode fromPinCode;
    @OneToOne
    private PinCode toPinCode;
}
