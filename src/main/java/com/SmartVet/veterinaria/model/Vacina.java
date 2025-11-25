package com.healthpet.veterinaria.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Classe Vacina - Entidade JPA
 * 
 * Representa uma vacina aplicada em um animal.
 * Controla datas de aplicação, próximas doses e alertas.
 * 
 * Relacionamento: N:1 com Animal (um animal pode ter várias vacinas)
 * 
 * @author Felipe Brito
 * @version 1.0
 */
@Entity
@Table(name = "vacinas")
public class Vacina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Relacionamento com Animal (N:1)
     * @JsonIgnore previne loop infinito na serialização JSON
     */
    @ManyToOne
    @JoinColumn(name = "animal_id", nullable = false)
    @JsonIgnore
    private Animal animal;

    /**
     * Nome da vacina
     * Ex: V8, V10, Antirrábica, Gripe Felina
     */
    @NotBlank(message = "Nome da vacina é obrigatório")
    @Column(nullable = false, length = 100)
    private String nome;

    /**
     * Data de aplicação da vacina
     */
    @NotNull(message = "Data de aplicação é obrigatória")
    @Column(name = "data_aplicacao", nullable = false)
    private LocalDate dataAplicacao;

    /**
     * Data da próxima dose (se houver)
     */
    @Column(name = "proxima_dose")
    private LocalDate proximaDose;

    /**
     * Lote da vacina
     */
    @Column(length = 50)
    private String lote;

    /**
     * Nome do veterinário que aplicou
     */
    @Column(name = "veterinario", length = 100)
    private String veterinario;

    /**
     * Observações sobre a aplicação
     */
    @Column(length = 500)
    private String observacoes;

    /**
     * Se a vacina já foi aplicada (todas as doses)
     */
    @Column(nullable = false)
    private Boolean completa = false;

    // ========== CONSTRUTORES ==========

    public Vacina() {
    }

    public Vacina(String nome, LocalDate dataAplicacao) {
        this.nome = nome;
        this.dataAplicacao = dataAplicacao;
    }

    public Vacina(Animal animal, String nome, LocalDate dataAplicacao) {
        this.animal = animal;
        this.nome = nome;
        this.dataAplicacao = dataAplicacao;
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataAplicacao() {
        return dataAplicacao;
    }

    public void setDataAplicacao(LocalDate dataAplicacao) {
        this.dataAplicacao = dataAplicacao;
    }

    public LocalDate getProximaDose() {
        return proximaDose;
    }

    public void setProximaDose(LocalDate proximaDose) {
        this.proximaDose = proximaDose;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(String veterinario) {
        this.veterinario = veterinario;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Boolean getCompleta() {
        return completa;
    }

    public void setCompleta(Boolean completa) {
        this.completa = completa;
    }

    // ========== MÉTODOS PERSONALIZADOS ==========

    /**
     * Verifica se a próxima dose está vencida
     */
    public boolean isVencida() {
        if (proximaDose == null || completa) {
            return false;
        }
        return LocalDate.now().isAfter(proximaDose);
    }

    /**
     * Verifica se a próxima dose está próxima (dentro de 7 dias)
     */
    public boolean isProxima() {
        if (proximaDose == null || completa) {
            return false;
        }
        LocalDate hoje = LocalDate.now();
        return !proximaDose.isBefore(hoje) && 
               ChronoUnit.DAYS.between(hoje, proximaDose) <= 7;
    }

    /**
     * Retorna o status da vacina
     */
    public String getStatus() {
        if (completa) {
            return "Completa";
        }
        if (proximaDose == null) {
            return "Aplicada";
        }
        if (isVencida()) {
            return "Vencida";
        }
        if (isProxima()) {
            return "Próxima";
        }
        return "Em dia";
    }

    /**
     * Calcula quantos dias faltam para a próxima dose
     */
    public Long diasAteProximaDose() {
        if (proximaDose == null || completa) {
            return null;
        }
        return ChronoUnit.DAYS.between(LocalDate.now(), proximaDose);
    }

    /**
     * Retorna mensagem sobre a próxima dose
     */
    public String getMensagemProximaDose() {
        if (completa) {
            return "Vacinação completa";
        }
        if (proximaDose == null) {
            return "Sem próxima dose agendada";
        }
        
        Long dias = diasAteProximaDose();
        if (dias == null) {
            return "Sem próxima dose";
        }
        
        if (dias < 0) {
            return "Atrasada há " + Math.abs(dias) + " dia(s)";
        } else if (dias == 0) {
            return "Vence hoje!";
        } else if (dias == 1) {
            return "Vence amanhã";
        } else if (dias <= 7) {
            return "Vence em " + dias + " dias";
        } else {
            return "Próxima dose: " + proximaDose;
        }
    }

    @Override
    public String toString() {
        return "Vacina{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", dataAplicacao=" + dataAplicacao +
                ", proximaDose=" + proximaDose +
                ", status=" + getStatus() +
                '}';
    }
}