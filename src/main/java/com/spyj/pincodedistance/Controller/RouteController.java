package com.spyj.pincodedistance.Controller;

import com.spyj.pincodedistance.ControllerAdvices.Exceptions.MapsAPIException;
import com.spyj.pincodedistance.Dtos.RouteRequest;
import com.spyj.pincodedistance.Dtos.RouteResponse;
import com.spyj.pincodedistance.Service.Inf.IMapService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RouteController {

    private final IMapService mapService;

    public RouteController(IMapService mapService) {
        this.mapService = mapService;
    }

    @GetMapping("/routes")
    public ResponseEntity<List<RouteResponse>> getRoutes(@RequestBody RouteRequest routeRequest) throws MapsAPIException {
        List<RouteResponse> routeResponseList = mapService.getDirections(routeRequest.getFromPinCode(), routeRequest.getToPinCode());
        return ResponseEntity.ok(routeResponseList);
    }
}
