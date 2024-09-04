package com.example.mathsgenealogyapi.dissertation;

import com.example.mathsgenealogyapi.node.Node;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "dissertations")
public class Dissertation {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nodeid", nullable = false)
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

    @Column(name = "advisor1name")
    private String advisor1name;

    @Column(name = "advisor1id")
    private Long advisor1id; //Maths genealogy id

    @Column(name = "advisor2name")
    private String advisor2name;

    @Column(name = "advisor2id")
    private Long advisor2id; //Maths genealogy id
}
