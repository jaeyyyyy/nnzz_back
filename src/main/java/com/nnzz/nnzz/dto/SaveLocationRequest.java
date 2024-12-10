package com.nnzz.nnzz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SaveLocationRequest {
    private double lat;
    private double lng;
    private String address;
    private String buildingName;
}
