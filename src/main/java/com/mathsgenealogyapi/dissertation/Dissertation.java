package com.mathsgenealogyapi.dissertation;

import com.mathsgenealogyapi.edge.Edge;
import com.mathsgenealogyapi.node.Node;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "node_id", nullable = false)
    private Node node;

    @Column(name = "phdprefix")
    private String phdprefix;

    @Column(name = "university")
    private String university;

    @Column(name = "yearofcompletion")
    private Integer yearofcompletion;

    @Column(name = "dissertationtitle")
    private String dissertationtitle;

    @Column(name = "mscnumber")
    private String mscnumber; //Mathematics Subject Classification number in MSC202


    @Column(name="advisor1_id")
    private Integer advisor1_id;

    @Column(name="advisor2_id")
    private Integer advisor2_id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumns({
        @JoinColumn(name="advisor1_id", insertable = false, updatable = false, referencedColumnName = "fromNode_id"),
        @JoinColumn(name="node_id", insertable = false, updatable = false, referencedColumnName = "toNode_id")
    })
    private Edge advisor1edge;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name="advisor2_id", insertable = false, updatable = false, referencedColumnName = "fromNode_id"),
            @JoinColumn(name="node_id", insertable = false, updatable = false, referencedColumnName = "toNode_id")
    })
    private Edge advisor2edge;
}
