package com.mathsgenealogyapi.dissertation;

import com.mathsgenealogyapi.advisor.AdvisorDto;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link Dissertation}
 */
public record DissertationDto(Integer nodeId,
                              String nodeName,
                              String phdprefix,
                              String university,
                              String yearofcompletion,
                              String dissertationtitle,
                              String mscnumber,
                              List<AdvisorDto> advisors
) implements Serializable {
}