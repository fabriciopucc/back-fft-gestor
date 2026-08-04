package com.nft_gestor.nft_gestor.service;

import com.nft_gestor.nft_gestor.dto.request.SalvarDespesaRequestDTO;
import com.nft_gestor.nft_gestor.dto.request.LancarDespesaRequestDTO;
import com.nft_gestor.nft_gestor.exception.RequestException;
import com.nft_gestor.nft_gestor.model.*;
import com.nft_gestor.nft_gestor.repository.DespesaRepository;
import com.nft_gestor.nft_gestor.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DespesaService {

    @Autowired
    private DespesaRepository despesaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;


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

        if(usuario.getDias().isEmpty()){
            throw new RequestException("Desculpe, para lançar uma despea é necessário ter iniciado ao menos 1 dia!");
        }else{
            AcaoModel lancamento = new AcaoModel(
                null,
                "despesa",
                lancarDespesaRequestDTO.getHorario(),
                "despesa",
                lancarDespesaRequestDTO.getIcon(),
                lancarDespesaRequestDTO.getValor(),
                "",
                null
            );

            DiaModel ultimoDia = usuario.getDias().get(usuario.getDias().size() - 1);

            ultimoDia.getAcoes().add(lancamento);

            ultimoDia.setSaldoAtual(ultimoDia.getSaldoAtual() - lancarDespesaRequestDTO.getValor());
            usuario.setSaldoAtual(usuario.getSaldoAtual() - lancarDespesaRequestDTO.getValor());

            usuarioRepository.save(usuario);

            return "Despesa lançada com sucesso!";
        }
    }


    //Métodos privados
    private UsuarioModel buscarUsuarioPorCodigo(Long codigo){
        return usuarioRepository.findByCodigo(codigo)
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
}
