package com.nnzz.nnzz.repository;

import com.nnzz.nnzz.dto.SaveCardDTO;
import com.nnzz.nnzz.dto.ShowCardDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CardMapper {
    // food_type 기준으로 어느 카테고리인지 찾기
    int getSelectStoreCategory(String storeId);

    // 카드 저장하고 cardId 반환
    int createCard(SaveCardDTO card);

    String getCardDescription(Integer cardId);

    SaveCardDTO getCardInfo(Integer cardId);

    ShowCardDTO getCardFullInfo(Integer cardId);

}
