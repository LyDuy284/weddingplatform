package com.fu.weddingplatform.request.booking;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ServiceSupplierBookingDTO {
  private String serviceSupplierId;
  private int quantity;
  @Schema(type = "string", example = "yyyy-MM-ddTHH:mm:ss")
  private LocalDateTime dateCompleted;
  private String note;
}
