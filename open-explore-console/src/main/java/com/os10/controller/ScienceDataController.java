package com.os10.controller;

import com.os10.entity.ScienceDataDO;
import com.os10.dto.ScienceDataQuery;
import com.os10.service.ScienceDataServiceImpl;
import com.os10.util.BaseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sciencedata")
public class ScienceDataController {

    @Autowired
    private ScienceDataServiceImpl scienceDataService;

    @PostMapping("/v1/edit")
    public BaseMessage editScienceData(@RequestBody ScienceDataDO scienceDataDO){
        return scienceDataService.editScienceData(scienceDataDO);
    }

    @PostMapping("/v1/list")
    public BaseMessage listScienceData(@RequestBody ScienceDataQuery scienceDataQuery){
        return scienceDataService.listScienceData(scienceDataQuery);
    }

    @PostMapping("/v1/aggregations")
    public BaseMessage listScienceDataAggregations(@RequestBody ScienceDataQuery scienceDataQuery){
        return scienceDataService.listScienceDataAggregations(scienceDataQuery);
    }

    @GetMapping("/v1/getOne/{id}")
    public BaseMessage getScienceData(@PathVariable(name = "id") String id){
        return scienceDataService.getScienceData(id);
    }

}
