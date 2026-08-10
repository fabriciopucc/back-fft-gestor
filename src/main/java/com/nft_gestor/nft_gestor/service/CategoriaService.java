package com.nft_gestor.nft_gestor.service;

import com.nft_gestor.nft_gestor.dto.request.SalvarCategoriaRequestDTO;
import com.nft_gestor.nft_gestor.exception.RequestException;
import com.nft_gestor.nft_gestor.model.CategoriaModel;
import com.nft_gestor.nft_gestor.model.UsuarioModel;
import com.nft_gestor.nft_gestor.repository.CategoriaRepository;
import com.nft_gestor.nft_gestor.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;


    public List<CategoriaModel> listarCategoriasDeUmUsuario(Long codigo){
        UsuarioModel usuario = buscarUsuarioPorCodigo(codigo);

        return usuario.getCategorias();
    }

    public List<CategoriaModel>  salvarCategoria(SalvarCategoriaRequestDTO salvarCategoriaRequestDTO){
        UsuarioModel usuario = buscarUsuarioPorCodigo(salvarCategoriaRequestDTO.getCodigoUsuario());

        if(categoriaRepository.buscarCategoriaEmListaDeCategoriasDeUmUsuario(
            salvarCategoriaRequestDTO.getNome().toLowerCase(), usuario.getCodigo()).isPresent()
        ){
            throw new RequestException("Este usuário já cadastrou essa categoria!");
        }

        CategoriaModel categoria = new CategoriaModel(
            null,
            salvarCategoriaRequestDTO.getNome(),
            salvarCategoriaRequestDTO.getIndiceIcon()
        );

        usuario.getCategorias().add(categoria);
        usuarioRepository.save(usuario);

        return usuario.getCategorias();
    }

    public List<CategoriaModel> excluirCategoria(Long codigo){
        CategoriaModel categoria = buscarCategoriaPorCodigo(codigo);
        UsuarioModel usuario = buscarUsuarioPorCodigoDeCategoria(categoria.getCodigo());

        usuario.getCategorias().remove(categoria);
        categoriaRepository.delete(categoria);

        return usuario.getCategorias();
    }


    //Métodos privados
    private CategoriaModel buscarCategoriaPorCodigo(Long codigo){
        return categoriaRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RequestException("Categoria inexistente!"));
    }

    private UsuarioModel buscarUsuarioPorCodigo(Long codigo){
        return usuarioRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RequestException("Usuário inexistente!"));
    }

    private UsuarioModel buscarUsuarioPorCodigoDeCategoria(Long codigoCategoria){
        return usuarioRepository.buscarUsuarioPorCodigoDeCategoria(codigoCategoria)
                .orElseThrow(() -> new RequestException("Usuário inexistente!"));
    }
}
