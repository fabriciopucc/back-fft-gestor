package com.nft_gestor.nft_gestor.dto.response;

import com.nft_gestor.nft_gestor.model.PeriodoModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CartaoResponseDTO {

    private Long codigo;
    private String apelido;
    private Integer ultimosDigitos;
    private Double limiteTotal;
    private Double limiteUtilizado;
    private String cor;
}
