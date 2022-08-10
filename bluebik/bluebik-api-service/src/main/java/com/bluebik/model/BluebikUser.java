package com.bluebik.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BluebikUser {
    private Integer id;


    private String account;


    private String name;


    private Integer age;
}
