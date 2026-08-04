package com.nft_gestor.nft_gestor.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LancarDespesaRequestDTO {

    private Long codigoUsuario;
    private Double valor;
    private String horario;
    private String icon;
}
