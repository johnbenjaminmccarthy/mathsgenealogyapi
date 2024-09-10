package com.example.mathsgenealogyapi.node;

import com.example.mathsgenealogyapi.dissertation.DissertationDto;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.example.mathsgenealogyapi.node.Node}
 */
@Builder
@Value
public class NodeDto implements Serializable {
    Integer genealogyId;
    String name;
    List<DissertationDto> dissertations;
    Integer numberofdescendents;
    String students;
}