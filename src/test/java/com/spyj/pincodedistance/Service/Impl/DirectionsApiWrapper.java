package com.spyj.pincodedistance.Service.Impl;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;

public class DirectionsApiWrapper {
    public DirectionsApiRequest newRequest(GeoApiContext context) {
        return DirectionsApi.newRequest(context);
    }
}