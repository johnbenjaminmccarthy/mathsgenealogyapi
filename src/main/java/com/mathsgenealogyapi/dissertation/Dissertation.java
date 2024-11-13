package com.mathsgenealogyapi.dissertation;

import com.mathsgenealogyapi.advisor.Advisor;
import com.mathsgenealogyapi.node.Node;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "dissertations")
public class Dissertation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "node_id", nullable = false)
    private Node node;

    @Column(name = "phdprefix", columnDefinition = "text")
    private String phdprefix;

    @Column(name = "university", columnDefinition = "text")
    private String university;

    @Column(name = "yearofcompletion", columnDefinition = "text")
    private String yearofcompletion;

    @Column(name = "dissertationtitle", columnDefinition = "text")
    private String dissertationtitle;

    @Column(name = "mscnumber", columnDefinition = "text")
    private String mscnumber; //Mathematics Subject Classification number in MSC202

    @OneToMany(mappedBy = "dissertation", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Advisor> advisors;
}
