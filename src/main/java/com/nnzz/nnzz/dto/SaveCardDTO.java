package com.nnzz.nnzz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SaveCardDTO {
    private Integer cardId;
    private Integer userId;
    private String storeId;
    private Integer foodTypeId;
    private LocalDate cardDate;
    private String mealtime;
}
