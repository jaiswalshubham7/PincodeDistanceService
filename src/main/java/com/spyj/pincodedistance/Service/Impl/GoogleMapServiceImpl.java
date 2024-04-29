package com.spyj.pincodedistance.Service.Impl;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.TravelMode;
import com.spyj.pincodedistance.ControllerAdvices.Exceptions.MapsAPIException;
import com.spyj.pincodedistance.Dtos.RouteResponse;
import com.spyj.pincodedistance.Model.PinCode;
import com.spyj.pincodedistance.Model.Route;
import com.spyj.pincodedistance.Repository.PinCodeRepository;
import com.spyj.pincodedistance.Repository.RouteRepository;
import com.spyj.pincodedistance.Service.Inf.IMapService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class GoogleMapServiceImpl implements IMapService {
    private final GeoApiContext geoApiContext;
    private final RouteRepository routeRepository;
    private final PinCodeRepository pinCodeRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public GoogleMapServiceImpl(@Value("${google.maps.api.key}") String apiKey,
                                RouteRepository routeRepository,
                                PinCodeRepository pinCodeRepository,
                                RedisTemplate<String, Object> redisTemplate) {
        this.geoApiContext = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();
        this.routeRepository = routeRepository;
        this.pinCodeRepository = pinCodeRepository;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<RouteResponse> getDirections(String from, String to) throws MapsAPIException {

        String cacheKey = "routes_" + from + "_to_" + to;
        List<Route> cachedRoutes = (List<Route>) redisTemplate.opsForValue().get(cacheKey);

        if (cachedRoutes != null) {
            return getRouteResponse(cachedRoutes);
        }

        DirectionsResult directionsResult = null;
        try {
            directionsResult = DirectionsApi.newRequest(geoApiContext)
                    .origin(from)
                    .destination(to)
                    .mode(TravelMode.DRIVING)
                    .alternatives(true)  // Get multiple routes
                    .await();
        } catch (ApiException | IOException | InterruptedException e) {
            throw new MapsAPIException(e.getMessage());
        }

        List<Route> routes = getAllRoutesFromDirections(directionsResult, from, to);

        redisTemplate.opsForValue().set(cacheKey, routes, 1, TimeUnit.HOURS);

        return getRouteResponse(routes);
    }

    private List<Route> getAllRoutesFromDirections(DirectionsResult directionsResult, String from, String to){
        List<Route> routes = new ArrayList<>();
        for (DirectionsRoute directionsRoute : directionsResult.routes) {
            double totalDistance = 0;
            long totalDuration = 0;

            for (DirectionsLeg leg : directionsRoute.legs) {
                totalDistance += leg.distance.inMeters;
                totalDuration += leg.duration.inSeconds;
            }

            totalDistance = totalDistance / 1000;
            totalDuration = totalDuration / 3600;

            PinCode fromPinCode;
            PinCode toPinCode;

            Optional<PinCode> fromPinCodeOptional = pinCodeRepository.findByPinCode(from);
            if (fromPinCodeOptional.isEmpty()) {
                fromPinCode = PinCode.builder()
                        .pinCode(from)
                        .lat(String.valueOf(directionsRoute.legs[0].startLocation.lat))
                        .lon(String.valueOf(directionsRoute.legs[0].startLocation.lng))
                        .build();
                pinCodeRepository.save(fromPinCode);
            }else{
                fromPinCode = fromPinCodeOptional.get();
            }

            int routesLength = directionsRoute.legs.length;

            Optional<PinCode> toPinCodeOptional = pinCodeRepository.findByPinCode(to);
            if (toPinCodeOptional.isEmpty()) {
                toPinCode = PinCode.builder()
                        .pinCode(to)
                        .lat(String.valueOf(directionsRoute.legs[routesLength-1].endLocation.lat))
                        .lon(String.valueOf(directionsRoute.legs[routesLength-1].endLocation.lng))
                        .build();
                pinCodeRepository.save(toPinCode);
            }else {
                toPinCode = toPinCodeOptional.get();
            }

            Route route;
            Optional<Route> routeOptional = routeRepository.findByFromPinCodeAndToPinCode(fromPinCode, toPinCode);
            if (routeOptional.isPresent()){
                route = routeOptional.get();
            }else{
                route = Route.builder()
                        .routeName(directionsRoute.summary)
                        .totalDistance(totalDistance)
                        .totalDuration(totalDuration)
                        .fromPinCode(fromPinCode)
                        .toPinCode(toPinCode)
                        .build();

                routeRepository.save(route);
            }
            routes.add(route);
        }
        return routes;
    }
    private List<RouteResponse> getRouteResponse(List<Route> routes) {
        List<RouteResponse> routeResponses = new ArrayList<>();
        for (Route route : routes) {
            routeResponses.add(new RouteResponse(route.getRouteName(),
                    route.getTotalDistance() + " Km",
                    route.getTotalDuration() + " Hr"));
        }
        return routeResponses;
    }
}
