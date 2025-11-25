package com.healthpet.veterinaria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

/**
 * Classe Cachorro - SUBCLASSE de Animal com JPA
 * 
 * Representa especificamente um cachorro com persistência em banco de dados.
 * Herda todos os atributos e métodos da classe Animal.
 * 
 * @Entity - Marca como entidade JPA
 * @DiscriminatorValue - Valor que identifica esta classe na coluna discriminadora
 * 
 * @author Felipe Brito (adaptado para Spring Boot)
 * @version 2.0
 */
@Entity
@DiscriminatorValue("CACHORRO")
public class Cachorro extends Animal {

    // ========== ATRIBUTOS ESPECÍFICOS ==========

    /**
     * Porte do cachorro (Pequeno, Médio ou Grande)
     */
    @NotBlank(message = "Porte é obrigatório")
    @Column(length = 20)
    private String porte;

    // ========== CONSTRUTORES ==========

    /**
     * Construtor padrão (obrigatório para JPA)
     */
    public Cachorro() {
        super();
    }

    /**
     * Construtor completo
     */
    public Cachorro(String nome, String especie, Double idade, String nomeDono,
                    String telefone, String raca, String porte) {
        super(nome, especie, idade, nomeDono, telefone, raca);
        this.porte = porte;
    }

    // ========== GETTERS E SETTERS ==========

    public String getPorte() {
        return porte;
    }

    public void setPorte(String porte) {
        this.porte = porte;
    }

    // ========== MÉTODOS PERSONALIZADOS ==========

    /**
     * Calcula a idade do cachorro em "anos humanos"
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
     * Retorna recomendação de exercício baseado no porte
     */
    public String recomendarExercicio() {
        if (porte == null) return "Recomendação: Consulte o veterinário";
        
        switch (porte.toLowerCase()) {
            case "pequeno":
                return "Recomendação: 30 minutos de caminhada por dia";
            case "médio":
            case "medio":
                return "Recomendação: 1 hora de exercícios por dia";
            case "grande":
                return "Recomendação: 1h30 a 2 horas de exercícios por dia";
            default:
                return "Recomendação: Consulte o veterinário";
        }
    }

    // ========== SOBRESCRITA DE MÉTODOS ==========

    /**
     * Sobrescreve gerarFichaCompleta() adicionando dados de Cachorro
     */
    @Override
    public String gerarFichaCompleta() {
        String fichaBasica = super.gerarFichaCompleta();
        
        StringBuilder fichaCompleta = new StringBuilder(fichaBasica);
        fichaCompleta.append("\n--- Informações Específicas ---\n");
        fichaCompleta.append("Tipo: Cachorro\n");
        fichaCompleta.append("Porte: ").append(this.porte).append("\n");
        fichaCompleta.append("Idade Humana: ").append(calcularIdadeHumana()).append(" anos\n");
        fichaCompleta.append(recomendarExercicio()).append("\n");
        fichaCompleta.append("=====================================");
        
        return fichaCompleta.toString();
    }

    /**
     * Sobrescreve toString() adicionando o porte
     */
    @Override
    public String toString() {
        return super.toString() + " | Porte: " + porte;
    }

    /**
     * Sobrescreve getTipoAnimal() para identificação
     */
    @Override
    @Transient
    public String getTipoAnimal() {
        return "Cachorro";
    }
}