package com.mathsgenealogyapi.edge;

import java.io.Serializable;

/**
 * DTO for {@link Edge}
 */
public record EdgeDto(
        Integer fromNodeId,
        Integer toNodeId
) implements Serializable {
}