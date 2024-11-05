package com.mathsgenealogyapi.advisor;

import com.mathsgenealogyapi.dissertation.Dissertation;
import com.mathsgenealogyapi.edge.Edge;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
public class AdvisorId implements Serializable {
    private Dissertation dissertation;
    private Edge advisorEdge;

    public AdvisorId(Dissertation dissertation, Edge advisorEdge) {
        this.dissertation = dissertation;
        this.advisorEdge = advisorEdge;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        else if(!(obj instanceof Advisor)) {
            return false;
        }
        else {
            Advisor ob = (Advisor) obj;
            return (Objects.equals(ob.getDissertation().getId(), this.dissertation.getId()) && Objects.equals(ob.getAdvisorEdge(), this.advisorEdge));
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getDissertation().getId(), this.advisorEdge);
    }
}
