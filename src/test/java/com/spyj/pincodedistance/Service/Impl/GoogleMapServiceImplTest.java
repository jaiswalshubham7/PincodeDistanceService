package com.spyj.pincodedistance.Service.Impl;

import com.spyj.pincodedistance.ControllerAdvices.Exceptions.MapsAPIException;
import com.spyj.pincodedistance.Dtos.RouteResponse;
import com.spyj.pincodedistance.Model.PinCode;
import com.spyj.pincodedistance.Model.Route;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoogleMapServiceImplTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;
    @InjectMocks
    private GoogleMapServiceImpl googleMapService;


    @Test
    public void testGetDirections_redisCaches() throws MapsAPIException {
//        Create a list of Route objects
        List<Route> routeList = new ArrayList<>();

        Route route = new Route();
        route.setRouteName("route");
        route.setTotalDistance(100.0);
        route.setTotalDuration(100);
        route.setFromPinCode(new PinCode("123456", "1.0", "1.0"));
        route.setToPinCode(new PinCode("654321", "2.0", "2.0"));

        Route route1 = new Route();
        route1.setRouteName("route1");
        route1.setTotalDistance(200.0);
        route1.setFromPinCode(new PinCode("123456", "1.0", "1.0"));
        route1.setToPinCode(new PinCode("654321", "2.0", "2.0"));

        routeList.add(route);
        routeList.add(route1);

//        Create a list of RouteResponse objects
        List<RouteResponse> routeResponseList = new ArrayList<>();
        RouteResponse routeResponse = new RouteResponse(route.getRouteName(), route.getTotalDistance() + " Km", route.getTotalDuration() + " Hr");
        RouteResponse routeResponse1 = new RouteResponse(route1.getRouteName(), route1.getTotalDistance() + " Km", route1.getTotalDuration() + " Hr");
        routeResponseList.add(routeResponse);
        routeResponseList.add(routeResponse1);

//        Mock the RedisTemplate
        ValueOperations<String, Object> valueOperations = mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("routes_123456_to_654321")).thenReturn(routeList);

//        Call the getDirections method
        List<RouteResponse> result = googleMapService.getDirections("123456", "654321");

//        Verify the result
        assertEquals(routeResponseList, result);
    }
}
