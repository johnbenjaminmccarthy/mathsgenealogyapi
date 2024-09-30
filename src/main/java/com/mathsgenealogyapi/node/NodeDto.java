package com.mathsgenealogyapi.node;

import com.mathsgenealogyapi.Student;
import com.mathsgenealogyapi.dissertation.DissertationDto;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link Node}
 */
public record NodeDto(Integer id,
                      String name,
                      List<DissertationDto> dissertations,
                      List<Student> students,
                      Integer numberofdescendents
) implements Serializable {
}