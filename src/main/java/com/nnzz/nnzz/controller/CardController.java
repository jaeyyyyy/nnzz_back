package com.nnzz.nnzz.controller;

import com.nnzz.nnzz.config.security.SecurityUtils;
import com.nnzz.nnzz.dto.CardRequest;
import com.nnzz.nnzz.dto.ResponseDetail;
import com.nnzz.nnzz.dto.SaveCardDTO;
import com.nnzz.nnzz.dto.ShowCardDTO;
import com.nnzz.nnzz.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
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

    /**
     * 최종선택 가게를 저장하고 보여줌
     * @param request
     * @return
     */
    @Operation(summary = "make card and show card", description = "<strong>\uD83D\uDCA1유저가 최종선택한 가게를 db에 저장하고 바로 보여줍니다</strong>")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "잘못된 형식의 날짜",
                content = @Content(schema = @Schema(implementation = ResponseDetail.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 상태에서 카드 생성 접근",
                content = @Content(schema = @Schema(implementation = ResponseDetail.class))),
    })
    @Parameters({
            @Parameter(name = "storeId", description = "String 타입, 최종선택한 가게의 storeId", required = true),
            @Parameter(name = "date", description = "String 타입, (예) yyyy-MM-dd 저녁", required = true)
    })
    @PostMapping("")
    public ResponseEntity<ShowCardDTO> makeCard(@RequestBody CardRequest request) {
        int authUserId = SecurityUtils.getUserId();

        String storeId = request.getStoreId(); // 가게아이디와
        String DateInput = request.getDate(); // 날짜를 받아옴

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

        int foodTypeId = cardService.getCategory(storeId);
        SaveCardDTO newCard = SaveCardDTO.builder()
                .userId(authUserId) // spring security를 통해서 로그인 한 유저의 id를 가져옴
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

                // .foodTypes(showCard.getFoodTypes())
                .foodTypeId(showCard.getFoodTypeId())
                .category(showCard.getCategory())
                .description(showCard.getDescription())

                // .stores(showCard.getStores())
                .storeId(showCard.getStoreId())
                .name(showCard.getName())
                .address(showCard.getAddress())
                .menus(showCard.getMenus())

                .cardDate(showCard.getCardDate())
                .mealtime(showCard.getMealtime())
                .createdAt(showCard.getCreatedAt())
                .build();

        return ResponseEntity.ok(updateCard);
    }
}
