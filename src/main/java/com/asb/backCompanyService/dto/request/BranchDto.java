package com.asb.backCompanyService.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchDto {
    private Long id;
    private String mainBranch;
    private String branchName;
    private String department;
    private String municipality;
    private String status;

}
