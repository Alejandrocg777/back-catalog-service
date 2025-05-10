package com.asb.backCompanyService.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchDtoRequest {

    private String branchName;
    private String mainBranch;
    private Long departmentId;
    private Long companyId;
    private Long municipalityId;
    private String status;

}
