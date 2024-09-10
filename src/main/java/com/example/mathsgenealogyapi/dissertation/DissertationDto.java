package com.example.mathsgenealogyapi.dissertation;

import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.example.mathsgenealogyapi.dissertation.Dissertation}
 */
@Builder
@Value
public class DissertationDto implements Serializable {
    String phdprefix;
    String university;
    Integer yearofcompletion;
    String dissertationtitle;
    String mscnumber;
    String advisor1name;
    Integer advisor1id;
    String advisor2name;
    Integer advisor2id;
}