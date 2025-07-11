package com.swp.drugprevention.backend.controller;

import com.swp.drugprevention.backend.io.request.ConsultantRequest;
import com.swp.drugprevention.backend.io.response.ConsultantResponse;
import com.swp.drugprevention.backend.model.Consultant;
import com.swp.drugprevention.backend.service.ConsultantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultant")
public class ConsultantController {
    @Autowired
    ConsultantService service;

    @GetMapping("/getAllConsultant")
    List<ConsultantResponse> getConsultant(){
        return service.getAllConsultants();
    }
    @GetMapping(value = "/getById/{consultantId}")
    ConsultantResponse getConsultant(@PathVariable("consultantId") Integer id){
        return service.getConsultantById(id);
    }

}