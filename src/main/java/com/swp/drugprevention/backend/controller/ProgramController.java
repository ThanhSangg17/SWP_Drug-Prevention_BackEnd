package com.swp.drugprevention.backend.controller;
import com.swp.drugprevention.backend.model.Program;
import com.swp.drugprevention.backend.service.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/programs")
public class ProgramController {
    @Autowired
    private ProgramService programService;

    @GetMapping("/getAllPrograms")
    public List<Program> getAllPrograms() {
        return programService.getAllPrograms();
    }

    @PostMapping("/createProgram")
    public Program createProgram(@RequestBody Program program) {
        return programService.saveProgram(program);
    }
}