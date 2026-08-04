package com.nft_gestor.nft_gestor.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "Periodo")
@Table(name = "periodos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PeriodoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    private Integer mes;
    private Integer ano;

    @OneToMany(
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JoinColumn(name = "compra_id")
    private List<CompraModel> compras = new ArrayList<>();
}
