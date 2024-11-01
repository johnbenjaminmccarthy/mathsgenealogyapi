package com.mathsgenealogyapi.node;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NodeWithGenerationsCount {
    private Node node;
    private Integer generationsCount;

    public NodeWithGenerationsCount(Object[] record){
        this.node = (Node)record[0];
        this.generationsCount = (Integer)record[1];
    }
}
