package com.example.mathsgenealogyapi.edge;

import java.io.Serializable;

public class EdgeId implements Serializable {
    private Long from;
    private Long to;

    public EdgeId(Long from, Long to) {
        this.from = from;
        this.to = to;
    }

}
