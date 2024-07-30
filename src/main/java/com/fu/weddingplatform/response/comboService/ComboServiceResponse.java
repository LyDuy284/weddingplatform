package com.fu.weddingplatform.response.comboService;

import com.fu.weddingplatform.entity.Combo;
import com.fu.weddingplatform.entity.Services;
import com.fu.weddingplatform.response.service.ServiceResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ComboServiceResponse {
    String id;
    String status;
    ServiceResponse service;
}
