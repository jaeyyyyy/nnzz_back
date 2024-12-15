package com.nnzz.nnzz.exception;

import lombok.Getter;

@Getter
public class AlreadyValidLocationException extends IllegalArgumentException {
    private final double lat;
    private final double lng;

    public AlreadyValidLocationException(double lat, double lng) {
        super("Valid location: " + lat + ", " + lng + " 이미 오픈된 지역입니다.");
        this.lat = lat;
        this.lng = lng;
    }
}
