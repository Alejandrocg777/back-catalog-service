package com.asb.backCompanyService.dto.responde;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class BranchDtoResponse {
        private Long id;
        private String mainBranch;
        private String branchName;
        private String departmentName;
        private String municipality;
        private String status;
        private String companyName;

    }


