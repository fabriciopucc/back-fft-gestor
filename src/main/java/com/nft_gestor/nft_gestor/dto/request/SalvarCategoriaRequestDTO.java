package com.nft_gestor.nft_gestor.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalvarCategoriaRequestDTO {

    private Long codigoUsuario;
    private String nome;
    private Integer indiceIcon;
}
