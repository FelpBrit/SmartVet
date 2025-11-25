package com.healthpet.veterinaria.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Classe Prontuario - Entidade JPA
 * 
 * Representa o prontuário médico completo de um animal.
 * Contém informações detalhadas sobre saúde, medidas e histórico.
 * 
 * Relacionamento: 1:1 com Animal (um animal tem um prontuário)
 * 
 * @author Felipe Brito
 * @version 1.0
 */
@Entity
@Table(name = "prontuarios")
public class Prontuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Relacionamento com Animal (1:1)
     * @JsonIgnore previne loop infinito na serialização JSON
     */
    @OneToOne
    @JoinColumn(name = "animal_id", nullable = false)
    @JsonIgnore
    private Animal animal;

    /**
     * Peso do animal em quilogramas
     */
    @Column(nullable = true)
    private Double peso;

    /**
     * Altura/comprimento do animal em centímetros
     */
    @Column(nullable = true)
    private Double altura;

    /**
     * Alergias conhecidas do animal
     * Formato: separadas por vírgula ou texto livre
     */
    @Column(length = 500)
    private String alergias;

    /**
     * Medicamentos em uso contínuo
     */
    @Column(length = 500)
    private String medicamentosEmUso;

    /**
     * Condições pré-existentes ou doenças crônicas
     */
    @Column(length = 1000)
    private String condicoesPreExistentes;

    /**
     * Observações gerais sobre o animal
     */
    @Column(length = 2000)
    private String observacoes;

    /**
     * Data da última atualização do prontuário
     */
    @Column(name = "ultima_atualizacao")
    private LocalDateTime ultimaAtualizacao;

    /**
     * Data de criação do prontuário
     */
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    // ========== CONSTRUTORES ==========

    public Prontuario() {
        this.dataCriacao = LocalDateTime.now();
        this.ultimaAtualizacao = LocalDateTime.now();
    }

    public Prontuario(Animal animal) {
        this();
        this.animal = animal;
    }

    // ========== MÉTODOS DE CICLO DE VIDA ==========

    @PreUpdate
    public void preUpdate() {
        this.ultimaAtualizacao = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        if (this.dataCriacao == null) {
            this.dataCriacao = LocalDateTime.now();
        }
        this.ultimaAtualizacao = LocalDateTime.now();
    }

    // ========== GETTERS E SETTERS ==========

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public Double getAltura() {
        return altura;
    }

    public void setAltura(Double altura) {
        this.altura = altura;
    }

    public String getAlergias() {
        return alergias;
    }

    public void setAlergias(String alergias) {
        this.alergias = alergias;
    }

    public String getMedicamentosEmUso() {
        return medicamentosEmUso;
    }

    public void setMedicamentosEmUso(String medicamentosEmUso) {
        this.medicamentosEmUso = medicamentosEmUso;
    }

    public String getCondicoesPreExistentes() {
        return condicoesPreExistentes;
    }

    public void setCondicoesPreExistentes(String condicoesPreExistentes) {
        this.condicoesPreExistentes = condicoesPreExistentes;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public LocalDateTime getUltimaAtualizacao() {
        return ultimaAtualizacao;
    }

    public void setUltimaAtualizacao(LocalDateTime ultimaAtualizacao) {
        this.ultimaAtualizacao = ultimaAtualizacao;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    // ========== MÉTODOS PERSONALIZADOS ==========

    /**
     * Calcula o IMC (Índice de Massa Corporal) adaptado para animais
     * Fórmula simplificada: peso / (altura/100)²
     */
    public Double calcularIMC() {
        if (peso == null || altura == null || altura == 0) {
            return null;
        }
        double alturaMetros = altura / 100.0;
        return peso / (alturaMetros * alturaMetros);
    }

    /**
     * Verifica se o prontuário tem informações preenchidas
     */
    public boolean temInformacoes() {
        return peso != null || altura != null || 
               (alergias != null && !alergias.trim().isEmpty()) ||
               (medicamentosEmUso != null && !medicamentosEmUso.trim().isEmpty()) ||
               (condicoesPreExistentes != null && !condicoesPreExistentes.trim().isEmpty());
    }

    /**
     * Retorna um resumo do prontuário
     */
    public String getResumo() {
        StringBuilder resumo = new StringBuilder();
        
        if (peso != null) {
            resumo.append("Peso: ").append(peso).append("kg");
        }
        
        if (altura != null) {
            if (resumo.length() > 0) resumo.append(" | ");
            resumo.append("Altura: ").append(altura).append("cm");
        }
        
        if (alergias != null && !alergias.trim().isEmpty()) {
            if (resumo.length() > 0) resumo.append(" | ");
            resumo.append("Tem alergias");
        }
        
        if (medicamentosEmUso != null && !medicamentosEmUso.trim().isEmpty()) {
            if (resumo.length() > 0) resumo.append(" | ");
            resumo.append("Medicação contínua");
        }
        
        return resumo.length() > 0 ? resumo.toString() : "Sem informações";
    }

    @Override
    public String toString() {
        return "Prontuario{" +
                "id=" + id +
                ", peso=" + peso +
                ", altura=" + altura +
                ", alergias='" + alergias + '\'' +
                ", medicamentosEmUso='" + medicamentosEmUso + '\'' +
                ", ultimaAtualizacao=" + ultimaAtualizacao +
                '}';
    }
}