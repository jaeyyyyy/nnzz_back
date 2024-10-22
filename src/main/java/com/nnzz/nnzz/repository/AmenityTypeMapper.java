package com.nnzz.nnzz.repository;

import com.nnzz.nnzz.model.AmenityType;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AmenityTypeMapper {
    String getAmenityName(String id);
}
