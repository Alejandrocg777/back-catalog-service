package com.asb.backCompanyService.dto.responde;

import com.asb.backCompanyService.dto.request.UserRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
   private List<UserListResponseDTO> users;
   private Long numberUser;
   private String status;

}
