package com.mathsgenealogyapi.scraper;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


public record ScrapedDissertationData (
    Integer studentId,
    String studentName,
    String phdprefix,
    String university,
    String yearofcompletion,
    String dissertationtitle,
    String mscnumber,
    String advisor1name,
    Integer advisor1id,
    String advisor2name,
    Integer advisor2id
) {
    
}
