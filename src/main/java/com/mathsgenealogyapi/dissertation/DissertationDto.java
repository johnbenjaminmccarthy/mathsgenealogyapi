package com.mathsgenealogyapi.dissertation;

import java.io.Serializable;

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
                              Integer advisor1edgeFromNodeId,
                              String advisor1edgeFromNodeName,
                              Integer advisor2edgeFromNodeId,
                              String advisor2edgeFromNodeName
) implements Serializable {
}