package com.nnzz.nnzz.exception;

import lombok.Getter;

@Getter
public class AlreadyRequestedLocationException extends IllegalArgumentException {
    private final double lat;
    private final double lng;

    public AlreadyRequestedLocationException(double lat, double lng) {
        super("Valid location: " + lat + ", " + lng + " 이미 오픈 요청한 지역입니다.");
        this.lat = lat;
        this.lng = lng;
    }
}
