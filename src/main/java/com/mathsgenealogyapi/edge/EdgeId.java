package com.mathsgenealogyapi.edge;

import com.mathsgenealogyapi.node.Node;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;


@NoArgsConstructor
@Getter
@Setter
public class EdgeId implements Serializable {
    private Node fromNode;
    private Node toNode;

    public EdgeId(Node fromNode, Node toNode) {
        this.fromNode = fromNode;
        this.toNode = toNode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        else if (!(obj instanceof Edge)) {
            return false;
        }
        else {
            Edge ob = (Edge) obj;
            return (Objects.equals(ob.getFromNode().getId(), this.getFromNode().getId())) && (Objects.equals(ob.getToNode().getId(), this.getToNode().getId()));
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.fromNode.getId(), this.toNode.getId());
    }

}
