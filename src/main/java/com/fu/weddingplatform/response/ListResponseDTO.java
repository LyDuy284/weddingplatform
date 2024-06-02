package com.fu.weddingplatform.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ListResponseDTO<T> {
    private String message;
    private List<T> data;
    private String status;
}