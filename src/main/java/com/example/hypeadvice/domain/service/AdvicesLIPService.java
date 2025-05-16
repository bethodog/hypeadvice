package com.example.hypeadvice.domain.service;

import java.util.Date;

import org.apache.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.hypeadvice.domain.entity.Advice;
import com.example.hypeadvice.domain.exception.RecursoNaoEncontradoException;
import com.example.hypeadvice.domain.utils.Utils;
import com.example.hypeadvice.domain.vo.AdviceListVO;
import com.example.hypeadvice.domain.vo.AdviceVO;
import com.example.hypeadvice.domain.vo.Slip;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@Service
public class AdvicesLIPService {

    public Advice gerar() throws UnirestException {
        HttpResponse<String> response = Unirest.get("https://api.adviceslip.com/advice")
                .header("Accept-Language", "br")
                .header("Content-Type", "application/json")
                .asString();
        int status = response.getStatus();
        if (HttpStatus.SC_OK == status) {
            AdviceVO adviceVO = null;
            try {
                String body = response.getBody();
                adviceVO = Utils.jsonToObject(AdviceVO.class, body);
            } catch (Exception e) {
                throw new RuntimeException("Status Code" + status + ", message " + e.getMessage());
            }

            if (adviceVO != null) {
                Slip slip = adviceVO.getSlip();
                String adviceStr = slip.getAdvice();
                return new Advice(adviceStr);
            } else {
                throw new RuntimeException("Status Code" + status + ", message " + response.getStatusText());
            }
        }
        else {
            throw new RuntimeException("Status Code" + status + ", message " + response.getStatusText());
        }
    }

    public AdviceListVO buscarByDescricao(String descricao) throws UnirestException {
        HttpResponse<String> response = Unirest.get("https://api.adviceslip.com/advice/search/" + descricao)
                .header("Accept-Language", "br")
                .header("Content-Type", "application/json")
                .asString();
        int status = response.getStatus();
        if (HttpStatus.SC_OK == status) {
            AdviceListVO vo = null;
            try {
                String body = response.getBody();
                if (body.contains("No advice slips found matching that search term")) {
                    throw new RuntimeException("Status Code" + status + ", message: No advice slips found matching that search term");
                }
                vo = Utils.jsonToObject(AdviceListVO.class, body);
            } catch (Exception e) {
                throw new RuntimeException("Status Code" + status + ", message " + e.getMessage());
            }

            if (vo != null) {
                return vo;
            } else {
                throw new RuntimeException("Status Code" + status + ", message " + response.getStatusText());
            }
        }
        else {
            throw new RuntimeException("Status Code" + status + ", message " + response.getStatusText());
        }
    }
    
    public AdviceVO buscarById(Long slip_id) throws UnirestException {
        HttpResponse<String> response = Unirest.get("https://api.adviceslip.com/advice/" + slip_id)
                .header("Accept-Language", "br")
                .header("Content-Type", "application/json")
                .asString();
        int status = response.getStatus();
        if (HttpStatus.SC_OK == status) {
        	AdviceVO vo = null;
            try {
                String body = response.getBody();
                if (body.contains("Advice slip not found")) {
                    throw new RecursoNaoEncontradoException(" Advice slip not found. Busca do Conselho Id: " + slip_id);
                }
                vo = Utils.jsonToObject(AdviceVO.class, adicionarDate(body));
            } catch (Exception e) {
                throw new RuntimeException("Status Code" + status + ", message " + e.getMessage());
            }

            if (vo != null) {
                return vo;
            } else {
            	throw new RecursoNaoEncontradoException(" Advice slip not found. Busca do Conselho Id: " + slip_id);
            }
        }
        else {
            throw new RuntimeException("Status Code" + status + ", message " + response.getStatusText());
        }
    }
    
    private static String adicionarDate(String texto) {
        // Encontra a posição da última chave "}" dentro do objeto "slip"
        int lastBraceIndex = texto.lastIndexOf("}}");
        Date dateNow = new Date();
        
        if (lastBraceIndex != -1) {
            StringBuilder sb = new StringBuilder(texto);
            sb.insert(lastBraceIndex, ", \"date\": \"\"");
            return sb.toString();
        } else {
            // Caso a estrutura esperada não seja encontrada, retorna o texto original
            return texto;
        }
    }
}
