package com.mathsgenealogyapi.advisor;

import com.mathsgenealogyapi.dissertation.Dissertation;
import com.mathsgenealogyapi.edge.Edge;
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
public class AdvisorId implements Serializable {
    private Dissertation dissertation;
    private Integer advisorId;

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        else if(!(obj instanceof Advisor ob)) {
            return false;
        }
        else {
            return (Objects.equals(ob.getDissertation().getId(), this.dissertation.getId()) && Objects.equals(ob.getAdvisorId(), this.advisorId));
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getDissertation().getId(), this.advisorId);
    }
}
