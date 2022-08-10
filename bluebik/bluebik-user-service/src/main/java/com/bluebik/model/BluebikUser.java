package com.bluebik.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "BLUEBIK_USER")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BluebikUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq_gen")
    private Integer id;


    private String account;


    private String name;


    private Integer age;
}
