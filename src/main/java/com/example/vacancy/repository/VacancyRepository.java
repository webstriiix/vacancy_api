package com.example.vacancy.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.vacancy.model.Vacancy;

@Repository
public interface VacancyRepository extends MongoRepository<Vacancy, String> {
    List<Vacancy> findByExpiryDateAfter(LocalDate currentDate);
}
