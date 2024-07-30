package com.fu.weddingplatform.request.combo;


import com.fu.weddingplatform.constant.validation.ValidationMessage;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateComboService {
    @NotEmpty(message = "Combo name " + ValidationMessage.NOT_EMPTY)
    String name;
    @NotEmpty(message = "Combo description " + ValidationMessage.NOT_EMPTY)
    String description;
    @NotEmpty(message = "listServiceId " + ValidationMessage.NOT_EMPTY)
    List<String> listServiceId;
    @NotEmpty(message = "staffId " + ValidationMessage.NOT_EMPTY)
    String staffId;
}
