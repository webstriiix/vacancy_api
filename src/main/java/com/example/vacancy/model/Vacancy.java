package com.example.vacancy.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "vacancies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vacancy {
    @Id
    private String id;

    private String name;
    private String description;
    private Integer maxAge;
    private Integer minExperience;
    private Double salary;
    private LocalDate publishDate;
    private LocalDate expiryDate;
}
