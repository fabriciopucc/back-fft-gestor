package com.nft_gestor.nft_gestor.service;

import com.nft_gestor.nft_gestor.dto.request.DefinirSaldoRequestDTO;
import com.nft_gestor.nft_gestor.dto.response.BuscaLimitesResponseDTO;
import com.nft_gestor.nft_gestor.dto.response.SaldoReponseDTO;
import com.nft_gestor.nft_gestor.exception.RequestException;
import com.nft_gestor.nft_gestor.model.*;
import com.nft_gestor.nft_gestor.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaldoService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DespesaRepository despesaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private DiaRepository diaRepository;



    public SaldoReponseDTO buscarSaldoDeUmUsuarioPeloCodigo(Long codigo){
        UsuarioModel usuario = buscarUsuarioPorCodigo(codigo);

        BuscaLimitesResponseDTO buscaLimites = cartaoRepository.buscarTotalDeLimiteDeCreditoUtilizadoPorUmUsuario(usuario.getCodigo());

        return new SaldoReponseDTO(
            usuario.getSaldoInicial(),
            usuario.getSaldoAtual(),
            buscaLimites.getLimiteUtilizado(),
            buscaLimites.getLimiteTotal()
        );
    }

    public SaldoReponseDTO definirSaldoInicial(DefinirSaldoRequestDTO definirSaldoRequestDTO){
        UsuarioModel usuario = buscarUsuarioPorCodigo(definirSaldoRequestDTO.getCodigoUsuario());

        usuario.setSaldoInicial(definirSaldoRequestDTO.getSaldoInicial());
        usuario.setSaldoAtual(definirSaldoRequestDTO.getSaldoInicial());

        usuarioRepository.save(usuario);

        return new SaldoReponseDTO(
            usuario.getSaldoInicial(),
            usuario.getSaldoAtual(),
            0.0,
            0.0
        );
    }

    public SaldoReponseDTO reiniciarGestaoDeUmUsuario(Long codigo){
        UsuarioModel usuario = buscarUsuarioPorCodigo(codigo);

        usuario.setSaldoInicial(0.0);
        usuario.setSaldoAtual(0.0);

        usuario.getDespesas().clear();;
        usuario.getDias().clear();
        usuario.getCategorias().clear();
        usuario.getCartoes().clear();

        usuarioRepository.save(usuario);

        return new SaldoReponseDTO(
        0.0,
        0.0,
        0.0,
        0.0
        );
    }

    //Métodos privado
    private UsuarioModel buscarUsuarioPorCodigo(Long codigo){
        return usuarioRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RequestException("Usuário inexitente!"));
    }
}
