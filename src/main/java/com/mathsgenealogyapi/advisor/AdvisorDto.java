package com.mathsgenealogyapi.advisor;

import java.io.Serializable;

/**
 * DTO for {@link Advisor}
 */
public record AdvisorDto(Integer advisorId,
                         String advisorName,
                         Integer advisorNumber
) implements Serializable {
}