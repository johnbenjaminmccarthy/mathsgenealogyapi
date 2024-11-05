package com.mathsgenealogyapi.edge;

import com.mathsgenealogyapi.node.Node;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EdgeId implements Serializable {
    private Node fromNode;
    private Node toNode;

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        else if (!(obj instanceof Edge ob)) {
            return false;
        }
        else {
            return (Objects.equals(ob.getFromNode().getId(), this.getFromNode().getId())) && (Objects.equals(ob.getToNode().getId(), this.getToNode().getId()));
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.fromNode.getId(), this.toNode.getId());
    }

}
