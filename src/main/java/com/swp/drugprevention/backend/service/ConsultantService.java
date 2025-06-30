package com.swp.drugprevention.backend.service;

import com.swp.drugprevention.backend.io.request.UpdateConsultantProfileRequest;
import com.swp.drugprevention.backend.io.response.ConsultantResponse;
import com.swp.drugprevention.backend.io.response.ConsultantUpdateResponse;
import com.swp.drugprevention.backend.model.Consultant;
import com.swp.drugprevention.backend.model.User;
import com.swp.drugprevention.backend.repository.ConsultantRepository;
import com.swp.drugprevention.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ConsultantService {
    private final ConsultantRepository consultantRepository;
    private final UserRepository userRepository;

    public List<ConsultantResponse> getAllConsultants() {
        return consultantRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }


    public ConsultantResponse getConsultantById(Integer id){
        Consultant existingConsultant = consultantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consultant not found with id: " + id));
        return convertToResponse(existingConsultant);
    }


    public void deleteConsultant(Integer id){
        consultantRepository.deleteById(id);
    }

    public ConsultantResponse convertToResponse(Consultant consultant) {
        ConsultantResponse response = new ConsultantResponse();
        response.setConsultantId(consultant.getConsultantId());
        response.setName(consultant.getName());
        response.setEmail(consultant.getEmail());
        response.setAvailability(consultant.isAvailability());
        response.setSchedule(consultant.getSchedule());
        response.setSpecialization(consultant.getSpecialization());
        response.setYob(consultant.getYob());
        response.setPhone(consultant.getPhone());
        return response;
    }

    public ConsultantUpdateResponse getConsultantUpdateResponse(String email) {
        Consultant consultant = consultantRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Consultant not found with id: " + email));
        return convertToConsultantUpdateResponse(consultant);
    }

    public ConsultantUpdateResponse convertToConsultantUpdateResponse (Consultant consultant) {
        return ConsultantUpdateResponse.builder()
                .consultantId(consultant.getConsultantId())
                .name(consultant.getName())
                //.email(consultant.getEmail())
                .specialization(consultant.getSpecialization())
                .phone(consultant.getPhone())
                .yob(consultant.getYob())
                .build();
    }

    @Transactional
    public void updateConsultantProfile(Consultant consultant, UpdateConsultantProfileRequest request, User user) {
        boolean nameChanged = false;
        boolean yobChanged = false;
        boolean phoneChanged = false;
        if (request.getName() != null && !request.getName().isEmpty()) {
            consultant.setName(request.getName());
            nameChanged = true;
        }

        if (request.getYob() != null) {
            consultant.setYob(request.getYob());
            yobChanged = true;
        }

        if (request.getPhone() != null && !request.getPhone().isEmpty()) {
            consultant.setPhone(request.getPhone());
            phoneChanged = true;
        }

        if (request.getSpecialization() != null && !request.getSpecialization().isEmpty()) {
            consultant.setSpecialization(request.getSpecialization());
        }

        //consultant.setEmail(user.getEmail());
        // Đồng bộ với Users nếu name thay đổi
        if (nameChanged) user.setFullName(consultant.getName());
        if (yobChanged) user.setYob(consultant.getYob());
        if (phoneChanged) user.setPhone(consultant.getPhone());
        consultantRepository.save(consultant); // Lưu thay đổi vào Consultants
        userRepository.save(user); // Lưu thay đổi vào Users
    }
}