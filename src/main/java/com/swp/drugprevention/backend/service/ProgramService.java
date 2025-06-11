package com.swp.drugprevention.backend.service;

import com.swp.drugprevention.backend.model.Program;
import com.swp.drugprevention.backend.repository.ProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgramService {
    @Autowired
    private ProgramRepository programRepository;

    public List<Program> getAllPrograms() {
        return programRepository.findAll();
    }

    public Program saveProgram(Program program) {
        return programRepository.save(program);
    }
}
