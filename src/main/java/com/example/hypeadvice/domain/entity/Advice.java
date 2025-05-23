package com.example.hypeadvice.domain.entity;

import com.example.hypeadvice.domain.enums.AdviceTypeEnum;
import com.google.gson.annotations.Expose;
import lombok.Data;
import javax.persistence.*;

@Data
@javax.persistence.Entity
@Table(name = "advice")
public class Advice extends Entity {
    @Id
    @Column(name = "ID", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Expose
    @Column(name = "NOME", length = 100)
    private String nome;

    @Expose
    @Column(name = "DESCRICAO", columnDefinition = "TEXT", length = 1000, nullable = false)
    private String descricao;
    
    @Expose
    @Column(name = "TIPO", columnDefinition = "TEXT", length = 1000, nullable = false)
    private AdviceTypeEnum tipo;
    
    public Advice(String adviceStr, AdviceTypeEnum type) {
        this.descricao = adviceStr;
        this.tipo = type;
    }

    public Advice(String adviceStr) {
        this.descricao = adviceStr;
    }

    public Advice() {

    }
}
