package com.nnzz.nnzz.exception;

import lombok.Getter;

@Getter
public class InvalidLocationException extends IllegalArgumentException {
    private final double lat;
    private final double lng;

    public InvalidLocationException(double lat, double lng) {
        super("Invalid location: " + lat + ", " + lng + " 오픈되지 않은 지역입니다.");
        this.lat = lat;
        this.lng = lng;
    }
}
