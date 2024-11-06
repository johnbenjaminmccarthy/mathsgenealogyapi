package com.mathsgenealogyapi.util;

public record TestDataAdvisor(
        Integer advisor_number,
        String name,
        Integer dissertation_id,
        Integer advisor_edge_from_node_id,
        Integer advisor_edge_to_node_id
) {
}
