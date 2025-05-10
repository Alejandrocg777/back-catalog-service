    package com.asb.backCompanyService.dto.request;

    import lombok.Data;
    import lombok.NoArgsConstructor;
    import lombok.AllArgsConstructor;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class CityDto {
        private Long id;
        private String cityCode;
        private String  cityName;
        private Long departmentId;
        private String status;
    }
