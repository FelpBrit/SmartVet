package com.healthpet.veterinaria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Classe Vacina - Controle de Vacinação
 * 
 * Registra todas as vacinas aplicadas nos animais
 * Controla datas de aplicação e próximas doses
 * Gera alertas para vacinas vencidas
 * 
 * @Entity - Marca como entidade JPA
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
     * Animal que recebeu a vacina
     * ManyToOne = muitas vacinas para um animal
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id", nullable = false)
    @NotNull(message = "Animal é obrigatório")
    private Animal animal;

    /**
     * Nome da vacina
     * Exemplos: V10, Antirrábica, Giardia, etc.
     */
    @NotBlank(message = "Nome da vacina é obrigatório")
    @Column(nullable = false, length = 100)
    private String nomeVacina;

    /**
     * Lote/Fabricante da vacina
     */
    @Column(length = 100)
    private String lote;

    /**
     * Data de aplicação da vacina
     */
    @NotNull(message = "Data de aplicação é obrigatória")
    @PastOrPresent(message = "Data de aplicação não pode ser futura")
    @Column(nullable = false)
    private LocalDate dataAplicacao;

    /**
     * Data da próxima dose (se houver)
     */
    @Column
    private LocalDate proximaDose;

    /**
     * Veterinário responsável pela aplicação
     */
    @Column(length = 100)
    private String veterinarioResponsavel;

    /**
     * Observações sobre a aplicação
     */
    @Column(length = 500)
    private String observacoes;

    /**
     * Dose aplicada (1ª dose, 2ª dose, reforço, etc.)
     */
    @Column(length = 50)
    private String dose;

    /**
     * Status da vacinação
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private StatusVacina status;

    /**
     * Enum para status da vacina
     */
    public enum StatusVacina {
        EM_DIA("Em dia"),
        PROXIMA_VENCER("Próxima a vencer"),
        VENCIDA("Vencida"),
        AGUARDANDO_PROXIMA_DOSE("Aguardando próxima dose");

        private final String descricao;

        StatusVacina(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    // ========== CONSTRUTORES ==========

    public Vacina() {
        this.status = StatusVacina.EM_DIA;
    }

    public Vacina(Animal animal, String nomeVacina, LocalDate dataAplicacao) {
        this.animal = animal;
        this.nomeVacina = nomeVacina;
        this.dataAplicacao = dataAplicacao;
        this.status = StatusVacina.EM_DIA;
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

    public String getNomeVacina() {
        return nomeVacina;
    }

    public void setNomeVacina(String nomeVacina) {
        this.nomeVacina = nomeVacina;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
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

    public String getVeterinarioResponsavel() {
        return veterinarioResponsavel;
    }

    public void setVeterinarioResponsavel(String veterinarioResponsavel) {
        this.veterinarioResponsavel = veterinarioResponsavel;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public StatusVacina getStatus() {
        return status;
    }

    public void setStatus(StatusVacina status) {
        this.status = status;
    }

    // ========== MÉTODOS PERSONALIZADOS ==========

    /**
     * Verifica e atualiza o status da vacina baseado nas datas
     */
    public void atualizarStatus() {
        if (proximaDose == null) {
            this.status = StatusVacina.EM_DIA;
            return;
        }

        LocalDate hoje = LocalDate.now();
        long diasRestantes = ChronoUnit.DAYS.between(hoje, proximaDose);

        if (diasRestantes < 0) {
            this.status = StatusVacina.VENCIDA;
        } else if (diasRestantes <= 30) {
            this.status = StatusVacina.PROXIMA_VENCER;
        } else {
            this.status = StatusVacina.AGUARDANDO_PROXIMA_DOSE;
        }
    }

    /**
     * Calcula quantos dias faltam para a próxima dose
     */
    public long getDiasParaProximaDose() {
        if (proximaDose == null) {
            return -1;
        }
        return ChronoUnit.DAYS.between(LocalDate.now(), proximaDose);
    }

    /**
     * Verifica se a vacina está vencida
     */
    public boolean isVencida() {
        atualizarStatus();
        return this.status == StatusVacina.VENCIDA;
    }

    /**
     * Verifica se a vacina está próxima de vencer (30 dias)
     */
    public boolean isProximaVencer() {
        atualizarStatus();
        return this.status == StatusVacina.PROXIMA_VENCER;
    }

    /**
     * Retorna mensagem de alerta se necessário
     */
    public String getMensagemAlerta() {
        atualizarStatus();
        
        switch (this.status) {
            case VENCIDA:
                return "⚠️ URGENTE: Vacina vencida há " + Math.abs(getDiasParaProximaDose()) + " dias!";
            case PROXIMA_VENCER:
                return "⏰ ATENÇÃO: Vacina vence em " + getDiasParaProximaDose() + " dias";
            case AGUARDANDO_PROXIMA_DOSE:
                return "📅 Próxima dose em " + getDiasParaProximaDose() + " dias";
            default:
                return "✅ Vacinação em dia";
        }
    }

    // ========== MÉTODO toString() ==========

    @Override
    public String toString() {
        return "Vacina: " + nomeVacina +
                " | Aplicada em: " + dataAplicacao +
                " | Dose: " + (dose != null ? dose : "N/A") +
                " | Status: " + status.getDescricao();
    }
}
