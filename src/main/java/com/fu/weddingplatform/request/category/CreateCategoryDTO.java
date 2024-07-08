package com.fu.weddingplatform.request.category;

import javax.validation.constraints.NotEmpty;

import com.fu.weddingplatform.constant.validation.ValidationMessage;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCategoryDTO {
    @NotEmpty(message = "Category Name " + ValidationMessage.NOT_EMPTY)
    private String categoryName;
}
