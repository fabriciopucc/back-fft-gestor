package com.nft_gestor.nft_gestor.service;

import com.nft_gestor.nft_gestor.exception.RequestException;
import com.nft_gestor.nft_gestor.model.CodigoConfirmacaoModel;
import com.nft_gestor.nft_gestor.model.UsuarioModel;
import com.nft_gestor.nft_gestor.repository.CodigoConfirmacaoRepository;
import com.nft_gestor.nft_gestor.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class CodigoConfirmacaoService {

    @Autowired
    private CodigoConfirmacaoRepository codigoConfirmacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JavaMailSender mailSender;


    public CodigoConfirmacaoModel gerarEEnviarCodigoConfirmacao(String email){
        CodigoConfirmacaoModel codigoConfirmacao;

        Integer numeroAleatorio = new Random().nextInt(1000, 9999);
        LocalDateTime dataExpiracao = LocalDateTime.now().plusMinutes(10);

        if(codigoConfirmacaoRepository.findByEmail(email).isEmpty()) {
            codigoConfirmacao = new CodigoConfirmacaoModel(
                null,
                numeroAleatorio,
                email,
                dataExpiracao
            );
        }else{
            codigoConfirmacao = codigoConfirmacaoRepository.findByEmail(email).get();
            codigoConfirmacao.setCodigoConfirmacao(numeroAleatorio);
            codigoConfirmacao.setDataExpiracao(dataExpiracao);
        }

        enviarEmailComCodigoDoUsuario(email, numeroAleatorio, "Código de confirmação", "cadastro");

        return codigoConfirmacaoRepository.save(codigoConfirmacao);
    }


    //Métodos privados
    /*private Boolean enviarEmailComCodigoDoUsuario(String email, Integer codigo, String cabecalho, String acao) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject(String.format(cabecalho));
        message.setText("O seu código de " + acao + " é: " + codigo+"\nEle expira em 10 minutos!");

        try {
            mailSender.send(message);
            return true;
        } catch (Exception e) {
            throw new RequestException("Erro ao enviar o código de confirmação para o email informado!");
        }*/
    private Boolean enviarEmailComCodigoDoUsuario(
            String email,
            Integer codigo,
            String cabecalho,
            String acao
    ) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject(cabecalho);
        message.setText(
                "O seu código de " + acao + " é: " + codigo +
                        "\nEle expira em 10 minutos!"
        );

        try {
            mailSender.send(message);
            return true;

        } catch (Exception e) {
            e.printStackTrace();

            throw new RequestException(
                    "Erro ao enviar o código de confirmação para o email informado!"
            );
        }
    }
    }
}
