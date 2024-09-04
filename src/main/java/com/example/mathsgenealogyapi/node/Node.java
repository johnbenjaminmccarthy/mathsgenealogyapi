package com.example.mathsgenealogyapi.node;

import com.example.mathsgenealogyapi.dissertation.Dissertation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Table(name = "nodes")
public class Node {
    @Id
    @GeneratedValue
    private Long id; //Separate local id to store the case where a person has multiple PhD dissertations to record as separate entries in the database e.g. Leibniz https://genealogy.math.ndsu.nodak.edu/id.php?id=60985

    @Column(name = "genealogyId")
    private Long genealogyId; //The genealogy id on https://genealogy.math.ndsu.nodak.edu/

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "node")
    private List<Dissertation> dissertations;

    @Column(name = "numberofdescendents")
    private Integer numberofdescendents;

    @Column(name = "students")
    private String students; //Stored as a list of genealogyid separated by commas ,

    public List<Integer> getStudents() {
        if (this.students.isEmpty()) {
            return new ArrayList<Integer>();
        }
        else {
            return Arrays.asList(this.students.split(",")).stream().mapToInt(Integer::parseInt).boxed().toList();
        }
    }

    public void setStudents(List<Integer> students) {
        this.students = students.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "lastupdated")
    private LocalDateTime lastupdated;

}
