package com.nnzz.nnzz.service;


import com.nnzz.nnzz.repository.AmenityTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AmenityTypeService {
    private final AmenityTypeMapper amenityTypeMapper;

    @Autowired
    public AmenityTypeService(AmenityTypeMapper amenityTypeMapper) {
        this.amenityTypeMapper = amenityTypeMapper;
    }

    public String getAmenityTypeById(String id) {
        return amenityTypeMapper.getAmenityName(id);
    }
}
