package com.example.mathsgenealogyapi.edge;

import java.io.Serializable;

public class EdgeId implements Serializable {
    private Long fromid;
    private Long toid;

    public EdgeId(Long fromid, Long toid) {
        this.fromid = fromid;
        this.toid = toid;
    }

}
