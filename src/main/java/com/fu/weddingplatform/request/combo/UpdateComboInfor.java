package com.fu.weddingplatform.request.combo;

import javax.validation.constraints.NotEmpty;

import com.fu.weddingplatform.constant.validation.ValidationMessage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateComboInfor {
    @NotEmpty(message = "Combo Id " + ValidationMessage.NOT_EMPTY)
    private String id;
    @NotEmpty(message = "Combo name " + ValidationMessage.NOT_EMPTY)
    private String name;
    @NotEmpty(message = "Combo description " + ValidationMessage.NOT_EMPTY)
    private String description;
    private String image;
}
