package com.nft_gestor.nft_gestor.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity(name = "CodigoConfirmacao")
@Table(name = "codigosConfirmacoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CodigoConfirmacaoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    private Integer codigoConfirmacao;

    @Column(unique = true)
    private String email;

    private LocalDateTime dataExpiracao;
}
