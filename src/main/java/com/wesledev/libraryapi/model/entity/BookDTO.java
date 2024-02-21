package com.wesledev.libraryapi.model.entity;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {

    private Long id;

    @NotEmpty
    private String title;

    @NotEmpty
    private String author;

    @NotEmpty
    private String isbn;


}
