package com.nft_gestor.nft_gestor.service;

import com.nft_gestor.nft_gestor.dto.request.CriarDiaRequestDTO;
import com.nft_gestor.nft_gestor.exception.RequestException;
import com.nft_gestor.nft_gestor.model.DiaModel;
import com.nft_gestor.nft_gestor.model.UsuarioModel;
import com.nft_gestor.nft_gestor.repository.DiaRepository;
import com.nft_gestor.nft_gestor.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class DiaService {

    @Autowired
    private DiaRepository diaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;


    public DiaModel criarDia(CriarDiaRequestDTO criarDiaRequestDTO){
        UsuarioModel usuario = buscarUsuarioPorCodigo(criarDiaRequestDTO.getCodigoUsuario());

        LocalDate data = LocalDate.parse(criarDiaRequestDTO.getData());

        if (data.isAfter(LocalDate.now())) {
            throw new RequestException("Você só pode criar o dia atual ou anteriores!");
        }

        if(diaRepository.buscarDataEspecificaNosDiasDeUmUsuario(criarDiaRequestDTO.getCodigoUsuario(), LocalDate.parse(criarDiaRequestDTO.getData())).isPresent()) {
            throw new RequestException("Desculpe, o dia atual já foi criado!");
        }

        DiaModel dia = new DiaModel(
            null,
            data,
            usuario.getSaldoAtual(),
            usuario.getSaldoAtual(),
            new ArrayList<>(),
            new ArrayList<>()
        );

        usuario.getDias().add(dia);
        usuarioRepository.save(usuario);

        return dia;
    }

    public List<DiaModel> listarDiasDeUmUsuario(Long codigoUsuario){
        UsuarioModel usuario = buscarUsuarioPorCodigo(codigoUsuario);

        return diaRepository.listarDiasDeUmUsuario(usuario.getCodigo());
    }

    public String excluirDiaPeloCodigo(Long codigo){
        DiaModel dia = buscarDiaPorCodigo(codigo);
        diaRepository.delete(dia);
        return "Excluído com sucesso!";
    }

    public String excluirDias(){
        diaRepository.deleteAll();
        return "Dias excluídos com sucesso!";
    }

    //Métodos privados
    private DiaModel buscarDiaPorCodigo(Long codigo){
        return diaRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RequestException("Dia inexistente!"));
    }

    private UsuarioModel buscarUsuarioPorCodigo(Long codigo){
        return usuarioRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RequestException("Usuário inexistente!"));
    }
}
