package com.healthpet.veterinaria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

/**
 * Classe Animal - SUPERCLASSE com JPA
 * 
 * Entidade JPA que representa um animal genérico cadastrado na clínica veterinária.
 * Agora com anotações para persistência em banco de dados.
 * 
 * @Entity - Marca como entidade JPA (tabela no banco)
 * @Inheritance - Define estratégia de herança (SINGLE_TABLE = todas classes em uma tabela)
 * @DiscriminatorColumn - Coluna que identifica o tipo (Animal, Cachorro ou Gato)
 * 
 * @author Felipe Brito (adaptado para Spring Boot)
 * @version 2.0
 */
@Entity
@Table(name = "animais")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_animal", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("ANIMAL")
public class Animal {

    // ========== ATRIBUTOS ==========
    
    /**
     * ID único do animal (chave primária)
     * @GeneratedValue - Valor gerado automaticamente pelo banco
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome do animal
     * @NotBlank - Não pode ser vazio
     */
    @NotBlank(message = "Nome é obrigatório")
    @Column(nullable = false, length = 100)
    private String nome;

    /**
     * Espécie do animal (Ex: Canina, Felina, Ave, etc.)
     */
    @NotBlank(message = "Espécie é obrigatória")
    @Column(nullable = false, length = 50)
    private String especie;

    /**
     * Idade do animal em anos (aceita decimais para meses)
     */
    @NotNull(message = "Idade é obrigatória")
    @Positive(message = "Idade deve ser positiva")
    @Column(nullable = false)
    private Double idade;

    /**
     * Nome do proprietário/dono do animal
     */
    @NotBlank(message = "Nome do dono é obrigatório")
    @Column(name = "nome_dono", nullable = false, length = 100)
    private String nomeDono;

    /**
     * Telefone de contato do proprietário
     */
    @NotBlank(message = "Telefone é obrigatório")
    @Column(nullable = false, length = 20)
    private String telefone;

    /**
     * Raça do animal
     */
    @Column(length = 50)
    private String raca;

    // ========== CAMPOS DO PRONTUÁRIO COMPLETO ==========

    /**
     * Peso do animal em kg
     */
    @Column
    private Double peso;

    /**
     * Altura/comprimento do animal em cm
     */
    @Column
    private Double altura;

    /**
     * Alergias conhecidas do animal
     */
    @Column(length = 500)
    private String alergias;

    /**
     * Medicamentos em uso atualmente
     */
    @Column(length = 500)
    private String medicamentosEmUso;

    /**
     * Condições pré-existentes (doenças crônicas, etc)
     */
    @Column(length = 500)
    private String condicoesPreExistentes;

    // ========== CONSTRUTORES ==========

    /**
     * Construtor padrão (obrigatório para JPA)
     */
    public Animal() {
    }

    /**
     * Construtor completo
     */
    public Animal(String nome, String especie, Double idade, String nomeDono, 
                  String telefone, String raca) {
        this.nome = nome;
        this.especie = especie;
        this.idade = idade;
        this.nomeDono = nomeDono;
        this.telefone = telefone;
        this.raca = (raca == null || raca.trim().isEmpty()) ? "Não informado" : raca;
        this.peso = 0.0;
        this.altura = 0.0;
        this.alergias = "Nenhuma alergia conhecida";
        this.medicamentosEmUso = "Nenhum";
        this.condicoesPreExistentes = "Nenhuma";
    }

    // ========== GETTERS E SETTERS ==========

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public Double getIdade() {
        return idade;
    }

    public void setIdade(Double idade) {
        this.idade = idade;
    }

    public String getNomeDono() {
        return nomeDono;
    }

    public void setNomeDono(String nomeDono) {
        this.nomeDono = nomeDono;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = (raca == null || raca.trim().isEmpty()) ? "Não informado" : raca;
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

    // ========== MÉTODOS PERSONALIZADOS (mantidos do código original) ==========

    /**
     * Converte a idade para o total de meses
     */
    public int calcularIdadeEmMeses() {
        return (int) Math.round(this.idade * 12);
    }

    /**
     * Retorna apenas os anos inteiros
     */
    public int getAnos() {
        return this.idade.intValue();
    }

    /**
     * Retorna apenas os meses adicionais
     */
    public int getMesesAdicionais() {
        int totalMeses = calcularIdadeEmMeses();
        int anos = getAnos();
        return totalMeses - (anos * 12);
    }

    /**
     * Retorna a idade formatada de forma legível
     */
    public String getIdadeFormatada() {
        int anos = getAnos();
        int meses = getMesesAdicionais();

        if (anos == 0) {
            return meses + " mês(es)";
        } else if (meses == 0) {
            return anos + " ano(s)";
        } else {
            return anos + " ano(s) e " + meses + " mês(es)";
        }
    }

    /**
     * Valida se a idade é válida
     */
    public boolean validarIdade() {
        return this.idade > 0 && this.idade <= 50;
    }

    /**
     * Gera uma ficha completa formatada do animal
     */
    public String gerarFichaCompleta() {
        StringBuilder ficha = new StringBuilder();
        ficha.append("========== FICHA DO ANIMAL ==========\n");
        ficha.append("ID: ").append(this.id).append("\n");
        ficha.append("Nome: ").append(this.nome).append("\n");
        ficha.append("Espécie: ").append(this.especie).append("\n");
        ficha.append("Raça: ").append(this.raca).append("\n");
        ficha.append("Idade: ").append(getIdadeFormatada());
        ficha.append(" (").append(calcularIdadeEmMeses()).append(" meses no total)\n");
        ficha.append("--- Dados do Proprietário ---\n");
        ficha.append("Nome: ").append(this.nomeDono).append("\n");
        ficha.append("Telefone: ").append(this.telefone).append("\n");
        ficha.append("=====================================");
        return ficha.toString();
    }

    /**
     * Método auxiliar para retornar o tipo do animal
     * Usado no frontend para identificar Cachorro/Gato/Animal
     */
    @Transient // Não é persistido no banco
    public String getTipoAnimal() {
        return "Animal";
    }

    // ========== MÉTODO toString() ==========

    @Override
    public String toString() {
        return "Animal: " + nome +
                " | Espécie: " + especie +
                " | Raça: " + raca +
                " | Idade: " + getIdadeFormatada() +
                " | Dono: " + nomeDono +
                " | Tel: " + telefone;
    }
}