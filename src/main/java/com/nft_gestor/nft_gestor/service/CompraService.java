package com.nft_gestor.nft_gestor.service;

import com.nft_gestor.nft_gestor.dto.request.AdicionarAcaoRequestDTO;
import com.nft_gestor.nft_gestor.exception.RequestException;
import com.nft_gestor.nft_gestor.model.CartaoModel;
import com.nft_gestor.nft_gestor.model.CompraModel;
import com.nft_gestor.nft_gestor.model.DiaModel;
import com.nft_gestor.nft_gestor.model.PeriodoModel;
import com.nft_gestor.nft_gestor.repository.CartaoRepository;
import com.nft_gestor.nft_gestor.repository.CompraRepository;
import com.nft_gestor.nft_gestor.repository.DiaRepository;
import com.nft_gestor.nft_gestor.repository.PeriodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CompraService {

    @Autowired
    private DiaRepository diaRepository;

    @Autowired
    private PeriodoRepository periodoRepository;

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private AcaoService acaoService;


    public List<CompraModel> listarComprasDeUmPeriodo(Long codigoPeriodo){
        PeriodoModel periodo = buscarPeriodoPorCodigo(codigoPeriodo);

        return  periodo.getCompras();
    }

    public List<CompraModel> quitarCompra(Long codigo){
        DiaModel diaAtual = buscarDiaAtualPorData(LocalDate.now());
        CompraModel compra = buscarCompraPorCodigo(codigo);
        CartaoModel cartao = buscarCartaoPorCodigoDeCompra(compra.getCodigo());
        PeriodoModel periodo = buscarPeriodoPorCodigoDeCompra(compra.getCodigo());

        if(compra.getQuitado()){
            throw new RequestException("Essa compra já foi quitada!");
        }

        compra.setQuitado(true);
        cartao.setLimiteUtilizado(cartao.getLimiteUtilizado() - compra.getValor());

        AdicionarAcaoRequestDTO adicionarAcaoRequestDTO = new AdicionarAcaoRequestDTO(
            diaAtual.getCodigo(),
            cartao.getCodigo(),
            cartao.getApelido(),
            compra.getDescricao(),
            "quitacaoCartao",
            0,
            compra.getValor(),
            compra.getIdCompra()
        );

        acaoService.adicionarAcaoEmUmDia(adicionarAcaoRequestDTO);

        cartaoRepository.save(cartao);

        return periodo.getCompras();
    }


    //Métodos privados
    private DiaModel buscarDiaAtualPorData(LocalDate data){
        return diaRepository.findByData(data)
                .orElseThrow(() -> new RequestException("O dia atual ainda não foi adicionado no menu Gestão. Para você possa quitar essa compra, adicione o dia atual no menu Gestão!"));
    }

    private CartaoModel buscarCartaoPorCodigoDeCompra(Long codigoCompra){
        return  cartaoRepository.buscarCartaoPorCodigoDeCompra(codigoCompra)
                .orElseThrow(() -> new RequestException("Cartão inexistente!"));
    }

    private PeriodoModel buscarPeriodoPorCodigoDeCompra(Long codigoCompra){
        return  periodoRepository.buscarPeriodoPorCodigoDeCompra(codigoCompra)
                .orElseThrow(() -> new RequestException("Periodo inexistente!"));
    }

    private PeriodoModel buscarPeriodoPorCodigo(Long codigo){
        return  periodoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RequestException("Periodo inexistente!"));
    }

    private CompraModel buscarCompraPorCodigo(Long codigo){
        return  compraRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RequestException("Compra inexistente!"));
    }
}
