package com.asb.backCompanyService.dto.responde;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardModulesDTO {
    private InventoryStatsDTO inventory;
    private PayrollStatsDTO payroll;
    private DeliveryStatsDTO deliveries;
    private SupplierStatsDTO suppliers;
    private ClientStatsDTO clients;
}