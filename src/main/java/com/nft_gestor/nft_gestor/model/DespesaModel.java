package com.nft_gestor.nft_gestor.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Despesa")
@Table(name = "despesas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DespesaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    private String descricao;
    private Integer diaVencimento;
    private Double valor;
}
