package com.nnzz.nnzz.service;

import com.nnzz.nnzz.dto.SaveCardDTO;
import com.nnzz.nnzz.dto.ShowCardDTO;
import com.nnzz.nnzz.repository.CardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardMapper cardMapper;

    public int getCategory(String storeId){
        int foodTypeId = cardMapper.getSelectStoreCategory(storeId);
        System.out.println("Retrieved foodTypeId: " + foodTypeId); // foodTypeId 확인차 추가
        return foodTypeId;
    }

    public int createCard(SaveCardDTO newCard) {
        return cardMapper.createCard(newCard);
    }

    public String getCardDescription(Integer cardId){
        return cardMapper.getCardDescription(cardId);
    }

    public SaveCardDTO getCard(Integer cardId){
        return cardMapper.getCardInfo(cardId);
    }

    public ShowCardDTO getFullCard(Integer cardId){
        return cardMapper.getCardFullInfo(cardId);
    }
}
