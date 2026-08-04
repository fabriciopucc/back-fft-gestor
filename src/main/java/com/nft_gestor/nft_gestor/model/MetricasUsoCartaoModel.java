package com.nft_gestor.nft_gestor.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "MetricasCartao")
@Table(name = "metricasCartoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MetricasUsoCartaoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    private Long codigoCartao;
    private String apelidoCartao;
    private Double limiteUsadoInicial;
    private Double limiteUsadoAtual;
    private Double limiteTotal;
}
