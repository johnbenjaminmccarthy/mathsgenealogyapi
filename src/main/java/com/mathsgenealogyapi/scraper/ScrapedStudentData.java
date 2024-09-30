package com.mathsgenealogyapi.scraper;

import com.mathsgenealogyapi.Student;


public record ScrapedStudentData (Student student, String university, Integer year, Integer numberofdescendents) {
}
