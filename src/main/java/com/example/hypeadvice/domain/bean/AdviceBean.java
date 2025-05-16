package com.example.hypeadvice.domain.bean;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.hypeadvice.domain.entity.Advice;
import com.example.hypeadvice.domain.service.AdviceService;
import com.example.hypeadvice.domain.vo.AdviceVO;
import com.mashape.unirest.http.exceptions.UnirestException;

@Named
@ViewScoped
public class AdviceBean extends Bean {

    @Autowired AdviceService adviceService;

    private Advice advice = new Advice();
    private List<Advice> advices;
    private AdviceVO adviceVO;

    public void initBean() {
        advices = adviceService.findAll();
    }
    
    public void buscarId() {
        try {
            this.adviceVO = adviceService.buscarId(advice);
            addFaceMessage(FacesMessage.SEVERITY_INFO, "Sucesso", null);
        } catch (Exception e) {
            addMessageError(e);
        }
    }

    public List<Advice> getAdvices() {
        return advices;
    }

    public void setAdvices(List<Advice> advices) {
        this.advices = advices;
    }

    public void salvar() {
        adviceService.save(advice);
        advices.add(advice);
        adicionarAdvice();
        addFaceMessage(FacesMessage.SEVERITY_INFO, "Sucesso", null);
    }

    public void gerar() {
        try {
            advice = adviceService.gerar();
        } catch (UnirestException e) {
            addMessageError(e);
        }
    }

    public void adicionarAdvice() {
        advice = new Advice();
    }

    public Advice getAdvice() {
        return advice;
    }

    public void setAdvice(Advice advice) {
        this.advice = advice;
    }

	public AdviceVO getAdviceVO() {
		return adviceVO;
	}

	public void setAdviceVO(AdviceVO adviceVO) {
		this.adviceVO = adviceVO;
	}
}
