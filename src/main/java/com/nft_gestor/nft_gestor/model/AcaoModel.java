package com.nft_gestor.nft_gestor.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Acao")
@Table(name = "acoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AcaoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long codigo;
    private String categoria;
    private String horario;
    private String tipoTransacao;
    private String icon;
    private Double valor;
    private String apelidoCartao;
    private Long idCompra;
}
