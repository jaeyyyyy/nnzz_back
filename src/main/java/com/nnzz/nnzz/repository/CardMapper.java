package com.nnzz.nnzz.repository;

import com.nnzz.nnzz.dto.SaveCardDTO;
import com.nnzz.nnzz.dto.ShowCardDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CardMapper {
    // food_type 기준으로 어느 카테고리인지 찾기
    Integer getSelectStoreCategory(@Param("storeId") String storeId);

    // 카드 저장하고 cardId 반환
    Integer createCard(SaveCardDTO card);

    String getCardDescription(@Param("cardId") Integer cardId);

    SaveCardDTO getCardInfo(@Param("cardId") Integer cardId);

    ShowCardDTO getCardFullInfo(@Param("cardId") Integer cardId);

}
