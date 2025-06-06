package com.swp.drugprevention.backend.controller;

import com.swp.drugprevention.backend.model.Consultant;
import com.swp.drugprevention.backend.service.ConsultantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultants")
public class ConsultantController {
    @Autowired
    private ConsultantService consultantService;

    @GetMapping("/getAllConsultants")
    public List<Consultant> getAllConsultants() {
        return consultantService.getAllConsultants();
    }
    @PostMapping("/createConsultant")
    public Consultant createConsultant(@RequestBody Consultant consultant) {
        return consultantService.saveConsultant(consultant);
    }
}
