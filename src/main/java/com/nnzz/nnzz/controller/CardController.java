package com.nnzz.nnzz.controller;

import com.nnzz.nnzz.config.security.SecurityUtils;
import com.nnzz.nnzz.dto.SaveCardDTO;
import com.nnzz.nnzz.dto.ShowCardDTO;
import com.nnzz.nnzz.exception.UnauthorizedException;
import com.nnzz.nnzz.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

@Tag(name="make card", description = "유저가 선택한 가게로 카드를 생성합니다")
@RestController
@RequestMapping("/api/card")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    @Operation(summary = "make card and show card", description = "db에 카드를 저장하고 바로 보여줍니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "잘못된 형식의 날짜"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 상태에서 카드 생성 접근")
    })
    @Parameters({
            @Parameter(name = "storeId", description = "String 타입, 최종선택한 가게의 storeId", required = true),
            @Parameter(name = "date", description = "String 타입, (예) yyyy-MM-dd 저녁", required = true)
    })
    @PostMapping("/make")
    // 카드 추가하고 카드를 바로 보여줍니다.
    public ResponseEntity<ShowCardDTO> makeCard(@RequestBody Map<String, String> requestBody) throws DateTimeParseException {
        Integer userId = SecurityUtils.getUserId();
        if (userId == null) {
            throw new UnauthorizedException("인증되지 않은 유저입니다.");
        }

        String storeId = requestBody.get("storeId"); // 가게아이디와
        String DateInput = requestBody.get("date"); // 날짜를 받아옴

        String[] parts = DateInput.split(" "); // 공백기준으로 분리
        String requestDate = parts[0];
        String mealtime = parts[1];

        // 받아온 날짜를 LocalDate로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date;
        try {
            date = LocalDate.parse(requestDate, formatter);
        } catch (DateTimeParseException e) {
            throw new DateTimeParseException("날짜 : " + DateInput +"유효하지 않은 날짜 형식입니다.", DateInput, 0);
        }

        Integer foodTypeId = cardService.getCategory(storeId);
        SaveCardDTO newCard = SaveCardDTO.builder()
                // .userId(1)
                .userId(userId) // spring security를 통해서 로그인 한 유저의 id를 가져옴
                .storeId(storeId)
                .foodTypeId(foodTypeId)
                .cardDate(date)
                .mealtime(mealtime)
                .build();
        cardService.createCard(newCard);
        System.out.println("cardId : " + newCard.getCardId());

        // 생성된 카드 조회
        ShowCardDTO showCard = cardService.getFullCard(newCard.getCardId());
        ShowCardDTO updateCard = ShowCardDTO.builder()
                .cardId(showCard.getCardId())
                .userId(showCard.getUserId())
                .foodTypes(showCard.getFoodTypes())
                .stores(showCard.getStores())
                .cardDate(showCard.getCardDate())
                .mealtime(showCard.getMealtime())
                .createdAt(showCard.getCreatedAt())
                .build();

        return ResponseEntity.ok(updateCard);
    }
}
