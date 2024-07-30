package com.fu.weddingplatform.response.combo;

import com.fu.weddingplatform.response.comboService.ComboServiceResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComboResponse {
    String id;
    String name;
    String description;
    String status;
    String image;
    List<ComboServiceResponse> comboServices;
    String staffId;
}
