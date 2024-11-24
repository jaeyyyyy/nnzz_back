package com.nnzz.nnzz.controller;

import com.nnzz.nnzz.dto.SaveCardDTO;
import com.nnzz.nnzz.dto.ShowCardDTO;
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

import java.util.Map;

@Tag(name="make card", description = "유저가 선택한 가게로 카드를 생성합니다")
@RestController
@RequestMapping("/api/card")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    @Operation(summary = "make card and show card", description = "db에 카드를 저장하고 바로 보여줍니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @Parameters({
            @Parameter(name = "storeId", description = "String 타입, 최종선택한 가게의 storeId", required = true),
            @Parameter(name = "date", description = "String 타입, (예) yyyy년 MM월 dd일 저녁. 연산을 추가로 하는 건 아니고 단순히 카드에 보여주기 위해서 필요", required = true)
    })
    @PostMapping("/make")
    // 카드 추가하고 카드를 바로 보여줍니다.
    public ResponseEntity<ShowCardDTO> makeCard(@RequestBody Map<String, String> requestBody) {
        String storeId = requestBody.get("storeId"); // 가게아이디와
        String date = requestBody.get("date"); // 날짜를 받아옴

        Integer foodTypeId = cardService.getCategory(storeId);
        SaveCardDTO newCard = SaveCardDTO.builder()
                .userId(1) // spring security를 통해서 로그인 한 유저의 id를 가져와야되지만, 테스트를 위해 고정된 값을 넣는 중
                .storeId(storeId)
                .foodTypeId(foodTypeId)
                .build();
        cardService.createCard(newCard);
        System.out.println("cardId : " + newCard.getCardId());

        // 생성된 카드 조회
        ShowCardDTO showCard = cardService.getFullCard(newCard.getCardId());
        ShowCardDTO updateCard = ShowCardDTO.builder()
                .cardId(showCard.getCardId())
                .userId(showCard.getUserId())
                .storeId(showCard.getStoreId())
                .foodTypeId(showCard.getFoodTypeId())
                .name(showCard.getName())
                .address(showCard.getAddress())
                .category(showCard.getCategory())
                .menus(showCard.getMenus())
                .date(date)
                .build();

        return ResponseEntity.ok(updateCard);
    }
}
