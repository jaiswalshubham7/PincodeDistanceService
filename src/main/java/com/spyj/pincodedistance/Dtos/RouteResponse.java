package com.spyj.pincodedistance.Dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class RouteResponse {
    private String routeName;
    private String totalDistance;
    private String totalDuration;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RouteResponse that)) return false;
        return Objects.equals(getRouteName(), that.getRouteName()) && Objects.equals(getTotalDistance(), that.getTotalDistance()) && Objects.equals(getTotalDuration(), that.getTotalDuration());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRouteName(), getTotalDistance(), getTotalDuration());
    }

}