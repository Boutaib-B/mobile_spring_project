package com.group8.projectpfe.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Challenge{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "challenge_id")
    private List<Team> Teams;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Sport sport;


    private String  logoPath;
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

}
