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
    private String name;
    @NotEmpty(message = "Combo description " + ValidationMessage.NOT_EMPTY)
    private String description;
    private String image;
    @NotEmpty(message = "listServiceSupplierId " + ValidationMessage.NOT_EMPTY)
    private List<String> listServiceSupplierId;
    @NotEmpty(message = "staffId " + ValidationMessage.NOT_EMPTY)
    private String staffId;
}
