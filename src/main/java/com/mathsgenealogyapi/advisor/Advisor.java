package com.mathsgenealogyapi.advisor;

import com.mathsgenealogyapi.dissertation.Dissertation;
import com.mathsgenealogyapi.edge.Edge;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(AdvisorId.class)
@Table(name = "advisors", uniqueConstraints = { @UniqueConstraint(columnNames = { "dissertation_id", "advisor_number" }) }) //An advisor for a given dissertation must have a unique number (i.e. 1st 2nd 3rd advisor)
public class Advisor {

    @Id
    @ManyToOne
    @JoinColumn(name="dissertation_id", nullable = false)
    private Dissertation dissertation;

    @Id
    @Column(name="advisor_id")
    private Integer advisorId;

    @Column(name = "name", columnDefinition = "text")
    private String name;

    @Column(name = "advisor_number")
    private Integer advisorNumber;
}
