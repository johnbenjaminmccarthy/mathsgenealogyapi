package com.mathsgenealogyapi.scraper;

import java.util.List;


public record ScrapedNodeData (
    Integer id,
    String name,
    Integer numberofdescendents,
    List<ScrapedDissertationData> dissertations,
    List<ScrapedStudentData> students
) {

}
