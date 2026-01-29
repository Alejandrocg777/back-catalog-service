package com.asb.backCompanyService.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderAllocationDetailDto {

    private Long id;                    // productId
    private String productName;
    private Integer quantity;           // cantidad total del pedido
    private Integer quantityPerTrip;    // assignedQuantity (cantidad asignada en este viaje)
    private Double price;
    private Double discount;
    private String imgProduct;
    private Double total;
    private Long pendingOrderDetailId;  // ID del detalle del pedido pendiente

    // Constructor para las consultas JPQL del repositorio
    // Los parámetros deben coincidir con el orden de la consulta:
    // [id, orderAllocationId, pendingOrderDetailId, productId, productName, assignedQuantity, unitPrice, total]
    public OrderAllocationDetailDto(
            Long id,                        // id del detalle de asignación
            Long orderAllocationId,         // no lo usamos en el DTO pero viene de la consulta
            Long pendingOrderDetailId,
            Long productId,
            String productName,
            Integer assignedQuantity,
            Double unitPrice,
            Double discount,
            String imgProduct,
            Double total
    ) {
        this.id = productId;
        this.productName = productName;
        this.quantity = 0;                        // No viene en la consulta
        this.quantityPerTrip = assignedQuantity;
        this.price = unitPrice;
        this.discount = discount;
        this.imgProduct = imgProduct;
        this.total = total;
        this.pendingOrderDetailId = pendingOrderDetailId;
    }
}