package com.nft_gestor.nft_gestor.service;

import com.nft_gestor.nft_gestor.dto.request.AdicionarAcaoRequestDTO;
import com.nft_gestor.nft_gestor.dto.request.SalvarDespesaRequestDTO;
import com.nft_gestor.nft_gestor.dto.request.LancarDespesaRequestDTO;
import com.nft_gestor.nft_gestor.exception.RequestException;
import com.nft_gestor.nft_gestor.model.*;
import com.nft_gestor.nft_gestor.repository.CartaoRepository;
import com.nft_gestor.nft_gestor.repository.DespesaRepository;
import com.nft_gestor.nft_gestor.repository.DiaRepository;
import com.nft_gestor.nft_gestor.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DespesaService {

    @Autowired
    private DespesaRepository despesaRepository;

    @Autowired
    private  UsuarioRepository usuarioRepository;

    @Autowired
    private DiaRepository diaRepository;

    @Autowired
    private CartaoRepository cartaoRepository;


    public List<DespesaModel> listarDespesasDeUmUsuario(Long codigo){
        UsuarioModel usuario = buscarUsuarioPorCodigo(codigo);
        return usuario.getDespesas();
    }

    public List<DespesaModel> salvarDespesaDeUmUsuario(SalvarDespesaRequestDTO salvarDespesaRequestDTO){
        UsuarioModel usuario = buscarUsuarioPorCodigo(salvarDespesaRequestDTO.getCodigoUsuario());

        DespesaModel despesa = new DespesaModel(
            null,
            salvarDespesaRequestDTO.getDescricao(),
            salvarDespesaRequestDTO.getDiaVencimento(),
            salvarDespesaRequestDTO.getValor()
        );

        usuario.getDespesas().add(despesa);
        usuarioRepository.save(usuario);

        return usuario.getDespesas();
    }

    public List<DespesaModel> excluirDespesaPorCodigo(Long codigo){
       DespesaModel despesa = buscarDespesaPorCodigo(codigo);
       UsuarioModel usuario = buscarUsuarioPorCodigoDeDespesa(despesa.getCodigo());

       usuario.getDespesas().remove(despesa);
       despesaRepository.delete(despesa);

       return usuario.getDespesas();
    }

    public String lancarDespesaDeUmUsuario(LancarDespesaRequestDTO lancarDespesaRequestDTO){
        UsuarioModel usuario = buscarUsuarioPorCodigo(lancarDespesaRequestDTO.getCodigoUsuario());
        DiaModel diaAtual = buscarDiaAtualPorData(LocalDate.now());
        CartaoModel cartao = buscarCartaoPorCodigo(lancarDespesaRequestDTO.getCodigoCartao());


        AdicionarAcaoRequestDTO adicionarAcaoRequest = new AdicionarAcaoRequestDTO(
            diaAtual.getCodigo(),
            cartao.getCodigo(),
            cartao.getApelido(),
            "despesa",
            lancarDespesaRequestDTO.getFormaPagamento(),
            lancarDespesaRequestDTO.getIcon(),
            lancarDespesaRequestDTO.getValor(),
            null
        );


        return  "Aa";
    }


    //Métodos privados
    private UsuarioModel buscarUsuarioPorCodigo(Long codigo){
        return usuarioRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RequestException("Usuário inexistente!"));
    }

    private CartaoModel buscarCartaoPorCodigo(Long codigo){
        return cartaoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RequestException("Usuário inexistente!"));
    }

    private UsuarioModel buscarUsuarioPorCodigoDeDespesa(Long codigoDespesa){
        return usuarioRepository.buscarUsuarioPorCodigoDeDespesa(codigoDespesa)
                .orElseThrow(() -> new RequestException("Usuário inexistente!"));
    }

    private DespesaModel buscarDespesaPorCodigo(Long codigo){
        return despesaRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RequestException("Despesa inexistente!"));
    }

    private DiaModel buscarDiaAtualPorData(LocalDate data){
        return diaRepository.findByData(data)
                .orElseThrow(() -> new RequestException("O dia atual ainda não foi adicionado no menu Gestão. Para você possa quitar essa compra, adicione o dia atual no menu Gestão!"));
    }
}
