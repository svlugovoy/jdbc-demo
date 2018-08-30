package com.svlugovoy.jdbcdemo.domain;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
@Builder
public class Director {
    private Long id;
    private String first_name;
    private String last_name;
    private LocalDate birthday;
    private Character gender;
    private String instagram;

    private List<Movie> movies;
}
