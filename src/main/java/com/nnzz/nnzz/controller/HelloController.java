package com.nnzz.nnzz.controller;

import com.nnzz.nnzz.service.AmenityTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class HelloController {
    @Autowired
    private AmenityTypeService amenityTypeService;

    @GetMapping("/test/{id}")
    public String test(@PathVariable String id) {
        if(amenityTypeService == null) {
            return "서비스가 주입되지 않았습니다.";
        }
        return "안녕하세요. 현재 서버의 시간은 " + new Date() + "입니다!" + "\n" +
                amenityTypeService.getAmenityTypeById(id);
    }

}
