package com.nft_gestor.nft_gestor.repository;

import com.nft_gestor.nft_gestor.model.DespesaModel;
import com.nft_gestor.nft_gestor.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DespesaRepository extends JpaRepository<DespesaModel, Long> {

    @Query
    Optional<DespesaModel> findByCodigo(Long codigo);
}
