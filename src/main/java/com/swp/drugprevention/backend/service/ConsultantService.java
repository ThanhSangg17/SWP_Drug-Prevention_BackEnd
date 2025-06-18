package com.swp.drugprevention.backend.service;

import com.swp.drugprevention.backend.model.Consultant;
import com.swp.drugprevention.backend.repository.ConsultantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultantService {
    @Autowired
    private ConsultantRepository consultantRepository;

    public List<Consultant> getAllConsultants() {
        return consultantRepository.findAll();
    }

    public Consultant saveConsultant(Consultant consultant){
        return consultantRepository.save(consultant);
    }
}
