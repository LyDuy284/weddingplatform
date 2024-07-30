package com.fu.weddingplatform.request.combo;

import com.fu.weddingplatform.constant.validation.ValidationMessage;
import lombok.*;

import javax.validation.constraints.NotEmpty;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateComboInfor {
    @NotEmpty(message = "Combo Id " + ValidationMessage.NOT_EMPTY)
    String id;
    @NotEmpty(message = "Combo name " + ValidationMessage.NOT_EMPTY)
    String name;
    @NotEmpty(message = "Combo description " + ValidationMessage.NOT_EMPTY)
    String description;
}
