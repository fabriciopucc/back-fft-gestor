package com.nft_gestor.nft_gestor.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Compra")
@Table(name = "compras")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompraModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    private Integer dia;
    private String descricao;
    private Double valor;
    private Boolean quitado;
    private Long idCompra;
}
