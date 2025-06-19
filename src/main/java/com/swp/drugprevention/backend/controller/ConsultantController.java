package com.swp.drugprevention.backend.controller;

import com.swp.drugprevention.backend.io.ConsultantRequest;
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

    @PostMapping("/create")

    Consultant create(@RequestBody ConsultantRequest request){
        return service.saveConsultant(request);
    }
    @GetMapping("/getAllConsultant")
    List<Consultant> getConsultant(){
        return service.getAllConsultants();
    }
    @GetMapping(value = "/getById/{consultantId}")
    Consultant getConsultant(@PathVariable("consultantId") Integer id){
        return service.getConsultantById(id);
    }
    @PutMapping("/update/{consultantId}")
    Consultant updateConsultant(@PathVariable("consultantId") Integer id,@RequestBody ConsultantRequest request){
        return service.updateConsultant(id, request);
    }
    @DeleteMapping("/delete/{consultantId}")
    String deleteConsultant(@PathVariable("consultantId") Integer id){
        service.deleteConsultant(id);
        return "Consultant has been deleted";
    }
}
