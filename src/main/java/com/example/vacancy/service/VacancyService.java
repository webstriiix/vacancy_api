package com.example.vacancy.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.vacancy.model.Vacancy;
import com.example.vacancy.repository.VacancyRepository;

@Service
public class VacancyService {
    @Autowired
    private VacancyRepository repository;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public Vacancy createVacancy(Vacancy vacancy) {

        try {
            logger.debug("Starting vacancy creation process");

            logger.debug("Received vacancy object: {}", vacancy);

            LocalDate now = LocalDate.now();
            logger.debug("Setting publish date to: {}", now);
            vacancy.setPublishDate(now);

            LocalDate expiryDate = now.plusDays(30);
            logger.debug("Setting expiry date to: {} (current date + 30 days)", expiryDate);
            vacancy.setExpiryDate(expiryDate);

            logger.debug("Attempting to save vacancy to repository");
            Vacancy savedVacancy = repository.save(vacancy);

            logger.info("Vacancy successfully created with ID: {}", savedVacancy.getId());
            logger.debug("Saved vacancy details: {}", savedVacancy);

            return savedVacancy;
        } catch (Exception e) {
            logger.error("Error while creating vacancy: {}", e.getMessage(), e);
            throw e;
        }
    }

    public List<Vacancy> getAllActiveVacancies() {
        return repository.findByExpiryDateAfter(LocalDate.now());
    }

    public List<Vacancy> getAllVacancies() {
        return repository.findAll();
    }

    public Vacancy updateVacancy(String id, Vacancy updatedVacancy) {
        logger.debug("Attempting to update vacancy with ID: {}", id);
        logger.debug("Updated vacancy data: {}", updatedVacancy);

        return repository.findById(id).map(existing -> {
            logger.debug("Found existing vacancy: {}", existing);

            existing.setName(updatedVacancy.getName());
            existing.setDescription(updatedVacancy.getDescription());
            existing.setMaxAge(updatedVacancy.getMaxAge());
            existing.setMinExperience(updatedVacancy.getMinExperience());
            existing.setSalary(updatedVacancy.getSalary());
            existing.setPublishDate(LocalDate.now());

            if (updatedVacancy.getExpiryDate() != null) {
                logger.debug("Setting expiry date from input: {}", updatedVacancy.getExpiryDate());
                existing.setExpiryDate(updatedVacancy.getExpiryDate());
            } else {
                LocalDate defaultExpiry = LocalDate.now().plusDays(30);
                logger.debug("No expiry date provided. Setting default expiry date: {}", defaultExpiry);
                existing.setExpiryDate(defaultExpiry);
            }

            Vacancy saved = repository.save(existing);
            logger.debug("Vacancy updated and saved: {}", saved);
            return saved;
        }).orElseThrow(() -> {
            logger.error("Vacancy with ID {} not found for update.", id);
            return new RuntimeException("Vacancy not found");
        });
    }

    public void deleteVacancy(String id) {
        logger.info("Attempting to delete vacancy with ID: {}", id);
        repository.deleteById(id);
        logger.info("Vacancy with ID {} deleted", id);
    }
}
