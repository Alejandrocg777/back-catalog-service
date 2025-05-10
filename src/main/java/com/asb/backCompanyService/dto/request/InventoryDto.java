package com.asb.backCompanyService.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDto {
    private Long id;
    private String nombreEquipo;
    private String numeroSerie;
    private Long numeroInventario;
    private String comentario;
    private String status;
    private Long idUsuario;
    private Long idFabricante;
    private Long idEstadosDispositivo;
}
