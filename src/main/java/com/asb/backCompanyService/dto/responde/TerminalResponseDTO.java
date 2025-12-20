package com.asb.backCompanyService.dto.responde;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TerminalResponseDTO {

   private Long id;
   private String name;
   private Long numerationId;
   private String prefix;
   private Integer initialNumber;
   private Integer finalNumber;
   private Long userId;
   private String userName;
   private Long numberUser;
   private String status;

}
