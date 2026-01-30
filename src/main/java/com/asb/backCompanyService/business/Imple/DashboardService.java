package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.dto.responde.*;
import com.asb.backCompanyService.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class DashboardService {

    private final ClientRepository clientRepository;
    private final BillRepository billRepository;
    private final PendingOrderRepository pendingOrderRepository;
    private final ProductRepository productRepository;
    private final EmployeeRepository employeeRepository;
    private final SupplierRepository supplierRepository;

    public DashboardResponseDTO getDashboardMetrics() {
        try {
            log.info("Iniciando carga de métricas del dashboard");

            // Métricas principales
            DashboardMetricsDTO metrics = new DashboardMetricsDTO();
            metrics.setSalesToday(getSalesToday());
            metrics.setPendingDeliveries(getPendingDeliveries());
            metrics.setPendingInvoices(getPendingInvoices());

            // Módulos
            DashboardModulesDTO modules = new DashboardModulesDTO();
            modules.setClients(getClientStats());
            modules.setInventory(getInventoryStats());
            modules.setPayroll(getPayrollStats());
            modules.setDeliveries(getDeliveryStats());
            modules.setSuppliers(getSupplierStats());

            DashboardResponseDTO response = new DashboardResponseDTO();
            response.setMetrics(metrics);
            response.setModules(modules);

            log.info("Dashboard metrics cargadas exitosamente");
            return response;
        } catch (Exception e) {
            log.error("Error al obtener métricas del dashboard: {}", e.getMessage(), e);
            throw new RuntimeException("No se pudieron cargar las métricas del dashboard", e);
        }
    }

    private SalesTodayDTO getSalesToday() {
        try {
            Double sales = billRepository.getSalesToday();
            log.info("Ventas del día: ${}", sales);
            return new SalesTodayDTO(sales);
        } catch (Exception e) {
            log.error("Error al obtener ventas del día: {}", e.getMessage());
            return new SalesTodayDTO(0.0);
        }
    }

    private PendingDeliveriesDTO getPendingDeliveries() {
        try {
            Long total = pendingOrderRepository.countPendingDeliveries();
            Long urgent = pendingOrderRepository.countUrgentDeliveries();
            log.info("Entregas pendientes: {} (urgentes: {})", total, urgent);
            return new PendingDeliveriesDTO(total, urgent);
        } catch (Exception e) {
            log.error("Error al obtener entregas pendientes: {}", e.getMessage());
            return new PendingDeliveriesDTO(0L, 0L);
        }
    }

    private PendingInvoicesDTO getPendingInvoices() {
        try {
            Long total = billRepository.countPendingInvoices();
            Long urgent = billRepository.countUrgentInvoices();
            log.info("Facturas pendientes: {} (urgentes: {})", total, urgent);
            return new PendingInvoicesDTO(total, urgent);
        } catch (Exception e) {
            log.error("Error al obtener facturas pendientes: {}", e.getMessage());
            return new PendingInvoicesDTO(0L, 0L);
        }
    }

    private ClientStatsDTO getClientStats() {
        try {
            Long totalClients = clientRepository.countActiveClients();
            Long pendingInvoices = billRepository.countClientsWithPendingInvoices();
            log.info("Clientes activos: {} (con facturas pendientes: {})", totalClients, pendingInvoices);
            return new ClientStatsDTO(totalClients, pendingInvoices);
        } catch (Exception e) {
            log.error("Error al obtener estadísticas de clientes: {}", e.getMessage());
            return new ClientStatsDTO(0L, 0L);
        }
    }

    private InventoryStatsDTO getInventoryStats() {
        try {
            Long total = productRepository.countActiveProducts();
            Long lowStock = productRepository.countLowStockProducts();
            log.info("Productos activos: {} (stock bajo: {})", total, lowStock);
            return new InventoryStatsDTO(total, lowStock);
        } catch (Exception e) {
            log.error("Error al obtener estadísticas de inventario: {}", e.getMessage());
            return new InventoryStatsDTO(0L, 0L);
        }
    }

    private PayrollStatsDTO getPayrollStats() {
        try {
            Long employees = employeeRepository.countActiveEmployees();
            Boolean pendingPayroll = employeeRepository.hasPendingPayroll();
            log.info("Empleados activos: {} (nómina pendiente: {})", employees, pendingPayroll);
            return new PayrollStatsDTO(employees, pendingPayroll);
        } catch (Exception e) {
            log.error("Error al obtener estadísticas de nómina: {}", e.getMessage());
            return new PayrollStatsDTO(0L, false);
        }
    }

    private DeliveryStatsDTO getDeliveryStats() {
        try {
            Long total = pendingOrderRepository.countAllPending();
            Long today = pendingOrderRepository.countPendingToday();
            log.info("Entregas pendientes totales: {} (hoy: {})", total, today);
            return new DeliveryStatsDTO(total, today);
        } catch (Exception e) {
            log.error("Error al obtener estadísticas de entregas: {}", e.getMessage());
            return new DeliveryStatsDTO(0L, 0L);
        }
    }

    private SupplierStatsDTO getSupplierStats() {
        try {
            Long total = supplierRepository.countActiveSuppliers();
            Long overdueOrders = supplierRepository.countSuppliersWithDebt();
            log.info("Proveedores activos: {} (con deuda: {})", total, overdueOrders);
            return new SupplierStatsDTO(total, overdueOrders);
        } catch (Exception e) {
            log.error("Error al obtener estadísticas de proveedores: {}", e.getMessage());
            return new SupplierStatsDTO(0L, 0L);
        }
    }
}