package com.nft_gestor.nft_gestor.service;

import com.nft_gestor.nft_gestor.dto.request.CriarCartaoRequestDTO;
import com.nft_gestor.nft_gestor.dto.request.LancarCompraNoCartaoRequestDTO;
import com.nft_gestor.nft_gestor.exception.RequestException;
import com.nft_gestor.nft_gestor.model.CartaoModel;
import com.nft_gestor.nft_gestor.model.CompraModel;
import com.nft_gestor.nft_gestor.model.PeriodoModel;
import com.nft_gestor.nft_gestor.model.UsuarioModel;
import com.nft_gestor.nft_gestor.repository.CartaoRepository;
import com.nft_gestor.nft_gestor.repository.CompraRepository;
import com.nft_gestor.nft_gestor.repository.PeriodoRepository;
import com.nft_gestor.nft_gestor.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class CartaoService {

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private PeriodoRepository periodoRepository;

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;


    public List<CartaoModel> listarCartoesDeUmUsuario(Long codigoUsuario){
        UsuarioModel usuario = buscarUsuarioPorCodigo(codigoUsuario);
        return usuario.getCartoes();
    }

    public CartaoModel buscarCartaoPorCodigo(Long codigo){
        return cartaoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RequestException("Cartão inexistente!"));
    }

    public CartaoModel buscarCartaoDeumUsuario(Long codigoUsuario, Long codigoCartao){
        UsuarioModel usuario = buscarUsuarioPorCodigo(codigoUsuario);
        CartaoModel cartao = buscarCartaoPorCodigo(codigoCartao);

        if(cartaoRepository.buscarCartaoDeUmUsuario(usuario.getCodigo(), cartao.getCodigo()).isPresent()){
            return cartao;
        }
        else{
            throw new RequestException("Você não pode acessar esse cartão!");
        }
    }

    public UsuarioModel criarCartaoParaUsuario(CriarCartaoRequestDTO criarCartaoRequest){
        UsuarioModel usuario = buscarUsuarioPorCodigo(criarCartaoRequest.getCodigoUsuario());

        if(cartaoRepository.buscarCartaoDeUmUsuarioPeloApelido(usuario.getCodigo(), criarCartaoRequest.getApelido().toUpperCase()).isPresent()){
            throw new RequestException("Você já possui um cartão com esse apelido!");
        }

        if(criarCartaoRequest.getApelido().length() > 6){
            throw new RequestException("O apelido só pode conter no máximo 6 caracteres!");
        }

        CartaoModel cartao = new CartaoModel(
            null,
            criarCartaoRequest.getApelido().toUpperCase(),
            criarCartaoRequest.getUltimosDigitos(),
            criarCartaoRequest.getLimiteTotal(),
            0.0,
            criarCartaoRequest.getCor(),
            new ArrayList<>()
        );

        usuario.getCartoes().add(cartao);
        usuarioRepository.save(usuario);
        return  usuario;
    }

    public Long lancarCompraNoCartao(LancarCompraNoCartaoRequestDTO lancarCompraNoCartaoRequest){
        CartaoModel cartao = buscarCartaoPorCodigo(lancarCompraNoCartaoRequest.getCodigoCartao());

        if(lancarCompraNoCartaoRequest.getValor() > (cartao.getLimiteTotal() - cartao.getLimiteUtilizado())){
            throw new RequestException("Você não possui limite suficiente nesse cartão para essa compra!");
        }else{
            cartao.setLimiteUtilizado(cartao.getLimiteUtilizado() + lancarCompraNoCartaoRequest.getValor());
        }

        LocalDate dataAtual = LocalDate.now();
        PeriodoModel periodo;

        Long idCompra = new Random().nextLong(10000, 99999);

        while(compraRepository.findByIdCompra(idCompra).isPresent()){
            idCompra = new Random().nextLong(10000, 99999);
        }

        CompraModel compra = new CompraModel(
            null,
            dataAtual.getDayOfMonth(),
            lancarCompraNoCartaoRequest.getCategoria(),
            lancarCompraNoCartaoRequest.getValor(),
            false,
            idCompra
        );

        if(periodoRepository.buscarPeriodoEmDeterminadoCartao(cartao.getCodigo(), dataAtual.getMonthValue(), dataAtual.getYear()).isPresent()){
            periodo = periodoRepository.buscarPeriodoEmDeterminadoCartao(cartao.getCodigo(), dataAtual.getMonthValue(), dataAtual.getYear()).get();
        }
        else{
            periodo = new PeriodoModel(
                null,
                dataAtual.getMonthValue(),
                dataAtual.getYear(),
                new ArrayList<>()
            );

            cartao.getPeriodos().add(periodo);
        }

        periodo.getCompras().add(compra);
        cartaoRepository.save(cartao);

        return idCompra;
    }

    public String excluirTodosCartoes(){
        cartaoRepository.deleteAll();
        return "Cartões excluídos com sucesso!";
    }


    //Métodos privados
    private UsuarioModel buscarUsuarioPorCodigo(Long codigo){
        return usuarioRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RequestException("Usuário inexistente!"));
    }
}
