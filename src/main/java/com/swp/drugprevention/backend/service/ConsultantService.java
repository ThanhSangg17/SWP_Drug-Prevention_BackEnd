package com.swp.drugprevention.backend.service;

import com.swp.drugprevention.backend.io.ConsultantRequest;
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

    public Consultant saveConsultant(ConsultantRequest request) {
        Consultant consultant = new Consultant();
        consultant.setName(request.getName());
        consultant.setEmail(request.getEmail());
        consultant.setSpecialization(request.getSpecialization());
        consultant.setAvailability(request.getAvailability());
        consultant.setSchedule(request.getSchedule());
        return consultantRepository.save(consultant);
    }
    public Consultant getConsultantById(Integer id){
        return consultantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consultant not found"));

    }
    public Consultant updateConsultant(Integer id, ConsultantRequest request){
        Consultant consultant = getConsultantById(id);
        consultant.setName(request.getName());
        consultant.setEmail(request.getEmail());
        consultant.setSpecialization(request.getSpecialization());
        consultant.setAvailability(request.getAvailability());
        consultant.setSchedule(request.getSchedule());
        return consultantRepository.save(consultant);
    }
    public void deleteConsultant(Integer id){
        consultantRepository.deleteById(id);
    }
}
