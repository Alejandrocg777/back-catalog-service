    package com.asb.backCompanyService.dto.request;

    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class GeneralRateNeighborhoodDto {
        private Long cityId;
        private Long departmentId;
        private String typeOperation;
        private Double rate;
        private String status;
    }
