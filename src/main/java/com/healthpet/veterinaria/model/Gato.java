package com.healthpet.veterinaria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

/**
 * Classe Gato - SUBCLASSE de Animal com JPA
 * 
 * Representa especificamente um gato com persistência em banco de dados.
 * Herda todos os atributos e métodos da classe Animal.
 * 
 * @Entity - Marca como entidade JPA
 * @DiscriminatorValue - Valor que identifica esta classe na coluna discriminadora
 * 
 * @author Felipe Brito (adaptado para Spring Boot)
 * @version 2.0
 */
@Entity
@DiscriminatorValue("GATO")
public class Gato extends Animal {

    // ========== ATRIBUTOS ESPECÍFICOS ==========

    /**
     * Tipo de pelagem do gato (Curta, Longa, Média)
     */
    @NotBlank(message = "Pelagem é obrigatória")
    @Column(length = 20)
    private String pelagem;

    /**
     * Temperamento do gato (Calmo, Ativo, Agressivo, Tímido, etc.)
     */
    @NotBlank(message = "Temperamento é obrigatório")
    @Column(length = 30)
    private String temperamento;

    // ========== CONSTRUTORES ==========

    /**
     * Construtor padrão (obrigatório para JPA)
     */
    public Gato() {
        super();
    }

    /**
     * Construtor completo
     */
    public Gato(String nome, String especie, Double idade, String nomeDono,
                String telefone, String raca, String pelagem, String temperamento) {
        super(nome, especie, idade, nomeDono, telefone, raca);
        this.pelagem = pelagem;
        this.temperamento = temperamento;
    }

    // ========== GETTERS E SETTERS ==========

    public String getPelagem() {
        return pelagem;
    }

    public void setPelagem(String pelagem) {
        this.pelagem = pelagem;
    }

    public String getTemperamento() {
        return temperamento;
    }

    public void setTemperamento(String temperamento) {
        this.temperamento = temperamento;
    }

    // ========== MÉTODOS PERSONALIZADOS ==========

    /**
     * Calcula a idade do gato em "anos humanos"
     */
    public int calcularIdadeHumana() {
        Double idadeAtual = getIdade();
        int anos = getAnos();

        if (idadeAtual <= 0) {
            return 0;
        } else if (anos == 0) {
            return (int) (15 * idadeAtual);
        } else if (anos == 1) {
            return 15 + (int) (9 * (idadeAtual - 1));
        } else if (anos == 2) {
            return 24 + (int) (4 * (idadeAtual - 2));
        } else {
            return 24 + ((anos - 2) * 4) + (int) (4 * (idadeAtual - anos));
        }
    }

    /**
     * Retorna recomendação de cuidados baseado na pelagem
     */
    public String recomendarCuidadosPelagem() {
        if (pelagem == null) return "Cuidados: Consulte o veterinário";
        
        switch (pelagem.toLowerCase()) {
            case "curta":
                return "Cuidados: Escovação semanal é suficiente";
            case "média":
            case "media":
                return "Cuidados: Escovação 2-3 vezes por semana";
            case "longa":
                return "Cuidados: Escovação diária recomendada para evitar nós";
            default:
                return "Cuidados: Consulte o veterinário para orientações";
        }
    }

    /**
     * Analisa se o gato precisa de atenção especial com base no temperamento
     */
    public String analisarTemperamento() {
        if (temperamento == null) return "ℹ️ Temperamento não informado";
        
        String temp = temperamento.toLowerCase();

        if (temp.contains("agressivo")) {
            return "⚠️ ATENÇÃO: Gato com temperamento agressivo - manusear com cuidado";
        } else if (temp.contains("tímido") || temp.contains("timido")) {
            return "ℹ️ Gato tímido - necessita ambiente calmo e paciência";
        } else if (temp.contains("ativo")) {
            return "✓ Gato ativo - precisa de estímulos e brincadeiras regulares";
        } else if (temp.contains("calmo")) {
            return "✓ Gato calmo - temperamento tranquilo e equilibrado";
        } else {
            return "ℹ️ Temperamento registrado: " + temperamento;
        }
    }

    // ========== SOBRESCRITA DE MÉTODOS ==========

    /**
     * Sobrescreve gerarFichaCompleta() adicionando dados de Gato
     */
    @Override
    public String gerarFichaCompleta() {
        String fichaBasica = super.gerarFichaCompleta();
        
        StringBuilder fichaCompleta = new StringBuilder(fichaBasica);
        fichaCompleta.append("\n--- Informações Específicas ---\n");
        fichaCompleta.append("Tipo: Gato\n");
        fichaCompleta.append("Pelagem: ").append(this.pelagem).append("\n");
        fichaCompleta.append("Temperamento: ").append(this.temperamento).append("\n");
        fichaCompleta.append("Idade Humana: ").append(calcularIdadeHumana()).append(" anos\n");
        fichaCompleta.append(recomendarCuidadosPelagem()).append("\n");
        fichaCompleta.append(analisarTemperamento()).append("\n");
        fichaCompleta.append("=====================================");
        
        return fichaCompleta.toString();
    }

    /**
     * Sobrescreve toString() adicionando pelagem e temperamento
     */
    @Override
    public String toString() {
        return super.toString() +
                " | Pelagem: " + pelagem +
                " | Temperamento: " + temperamento;
    }

    /**
     * Sobrescreve getTipoAnimal() para identificação
     */
    @Override
    @Transient
    public String getTipoAnimal() {
        return "Gato";
    }
}