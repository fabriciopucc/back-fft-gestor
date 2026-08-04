package com.nft_gestor.nft_gestor.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "Cartao")
@Table(name = "cartoes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartaoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    private String apelido;
    private Integer ultimosDigitos;
    private Double limiteTotal;
    private Double limiteUtilizado;
    private Integer cor;

    @OneToMany(
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JoinColumn(name = "periodo_id")
    private List<PeriodoModel> periodos = new ArrayList<>();

}
