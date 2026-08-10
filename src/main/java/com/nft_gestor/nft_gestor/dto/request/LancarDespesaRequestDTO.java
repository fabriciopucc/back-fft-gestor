package com.nft_gestor.nft_gestor.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LancarDespesaRequestDTO {

    private String formaPagamento;
    private Long codigoCartao;
    private Double valorALancar;
}
