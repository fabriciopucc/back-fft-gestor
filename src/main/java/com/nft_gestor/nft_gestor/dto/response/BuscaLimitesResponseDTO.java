package com.nft_gestor.nft_gestor.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BuscaLimitesResponseDTO {

    private Double limiteUtilizado;
    private Double limiteTotal;
}
