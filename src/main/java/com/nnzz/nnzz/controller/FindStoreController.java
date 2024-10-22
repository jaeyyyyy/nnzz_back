package com.nnzz.nnzz.controller;

import com.nnzz.nnzz.service.FindStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/find")
public class FindStoreController {

    @Autowired
    private FindStoreService findStoreService;


    @GetMapping("/lunch/category")
    public List<String> getLunchCategories() {
        double lng = 127.0276241;
        double lat = 37.4979526;
        List<String> lunchCategoryList = findStoreService.getLunchCategoriesByLocation(lat, lng);

        return lunchCategoryList;
    }

    @GetMapping("/dinner/category")
    public List<String> getDinnerCategories() {
        double lng = 127.0276241;
        double lat = 37.4979526;
        List<String> dinnerCategoryList = findStoreService.getDinnerCategoriesByLocation(lat, lng);

        return dinnerCategoryList;
    }

}
