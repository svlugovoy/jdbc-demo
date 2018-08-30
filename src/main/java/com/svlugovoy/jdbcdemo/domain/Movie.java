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
public class Movie {
    private Long id;
    private String name;
    private Integer yearOfCreation;
    private String  genre;

    private Director director;
    private List<Actor> actors;
}
