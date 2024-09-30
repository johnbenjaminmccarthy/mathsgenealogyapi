package com.mathsgenealogyapi.scraper;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


public record ScrapedNodeData (
    Integer id,
    String name,
    Integer numberofdescendents,
    List<ScrapedDissertationData> dissertations,
    List<ScrapedStudentData> students
) {

}
