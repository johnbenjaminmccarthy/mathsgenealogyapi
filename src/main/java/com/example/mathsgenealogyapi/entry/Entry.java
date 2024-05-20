package com.example.mathsgenealogyapi.entry;

import jakarta.persistence.*;
import lombok.AccessLevel;
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
@Table(name = "entries")
public class Entry {
    @Id
    private Long id; //The genealogy id

    @Column(name = "name")
    private String name;

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
    private Long advisor1id;

    @Column(name = "advisor2name")
    private String advisor2name;

    @Column(name = "advisor2id")
    private Long advisor2id;

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
