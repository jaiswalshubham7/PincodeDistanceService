package com.spyj.pincodedistance.Service.Inf;

import com.spyj.pincodedistance.ControllerAdvices.Exceptions.MapsAPIException;
import com.spyj.pincodedistance.Dtos.RouteResponse;

import java.util.List;

public interface IMapService {
    List<RouteResponse> getDirections(String from, String to) throws MapsAPIException;
}
