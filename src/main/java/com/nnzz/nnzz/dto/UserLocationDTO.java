package com.nnzz.nnzz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserLocationDTO {
    private int locationId;
    private int userId;
    private double lat;
    private double lng;
    private String address;
    private LocalDateTime createdAt;
}
