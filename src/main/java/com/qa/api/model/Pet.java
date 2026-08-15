package com.qa.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pet {

    private Long id;
    private Category category;
    private String name;
    @Builder.Default
    private List<String> photoUrls = new ArrayList<>();
    @Builder.Default
    private List<Tag> tags = new ArrayList<>();
    private PetStatus status;
}
