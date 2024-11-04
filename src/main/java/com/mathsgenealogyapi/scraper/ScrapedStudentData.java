package com.mathsgenealogyapi.scraper;

import com.mathsgenealogyapi.Student;


public record ScrapedStudentData (Student student, String university, String year, Integer numberofdescendents) {
}
