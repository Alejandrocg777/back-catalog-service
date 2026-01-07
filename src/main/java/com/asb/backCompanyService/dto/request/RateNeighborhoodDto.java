    package com.asb.backCompanyService.dto.request;

    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class RateNeighborhoodDto {
        private Long id;
        private Long cityId;
        private Long departmentId;
        private String neighborhood;
        private Double rate;
        private String status;
    }
