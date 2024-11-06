package com.mathsgenealogyapi.util;

import java.time.LocalDateTime;

public record TestDataNode(
        Integer genealogy_id,
        LocalDateTime lastupdated,
        String name,
        Integer numberofdescendents,
        Boolean scraped
) {

}
