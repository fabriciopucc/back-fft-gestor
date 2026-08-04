package com.nft_gestor.nft_gestor.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CriarDiaRequestDTO {

    private Long codigoUsuario;
    private String data;
    private Double saldoAtual;
}
