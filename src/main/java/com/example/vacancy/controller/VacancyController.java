package com.example.vacancy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.vacancy.model.Vacancy;
import com.example.vacancy.service.VacancyService;

@RestController
@RequestMapping("/api/vacancies")
public class VacancyController {
    @Autowired
    private VacancyService service;

    @PostMapping
    public ResponseEntity<Vacancy> createVacancy(@RequestBody Vacancy vacancy) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createVacancy(vacancy));
    }

    @GetMapping("/active")
    public List<Vacancy> getActiveVacancies() {
        return service.getAllActiveVacancies();
    }

    @GetMapping
    public List<Vacancy> getAllVacancies() {
        return service.getAllVacancies();
    }

    @PutMapping("/{id}")
    public Vacancy updateVacancy(@PathVariable String id, @RequestBody Vacancy vacancy) {
        return service.updateVacancy(id, vacancy);
    }

    @DeleteMapping("/{id}")
    public void deleteVacancy(@PathVariable String id) {
        service.deleteVacancy(id);
    }
}
