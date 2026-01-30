package com.asb.backCompanyService.dto.responde;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardMetricsDTO {
    private SalesTodayDTO salesToday;
    private PendingDeliveriesDTO pendingDeliveries;
    private PendingInvoicesDTO pendingInvoices;
}