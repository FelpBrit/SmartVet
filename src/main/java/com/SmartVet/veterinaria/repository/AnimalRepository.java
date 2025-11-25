package com.healthpet.veterinaria.repository;

import com.healthpet.veterinaria.model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * AnimalRepository - Interface de Acesso a Dados
 * 
 * Estende JpaRepository que fornece métodos CRUD prontos:
 * - save() - Salvar/Atualizar
 * - findById() - Buscar por ID
 * - findAll() - Listar todos
 * - deleteById() - Deletar por ID
 * - count() - Contar registros
 * 
 * Spring Data JPA cria a implementação automaticamente!
 * 
 * @author Felipe Brito
 * @version 1.0
 */
@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {

    /**
     * Busca animais por nome (case-insensitive)
     * Spring Data JPA cria a query automaticamente pelo nome do método!
     * 
     * Padrão: findBy + NomeDoAtributo + IgnoreCase
     */
    List<Animal> findByNomeContainingIgnoreCase(String nome);

    /**
     * Busca animal por nome exato (case-insensitive)
     */
    Optional<Animal> findByNomeIgnoreCase(String nome);

    /**
     * Busca animais por espécie
     */
    List<Animal> findByEspecieIgnoreCase(String especie);

    /**
     * Busca animais por nome do dono
     */
    List<Animal> findByNomeDonoContainingIgnoreCase(String nomeDono);

    /**
     * Busca animais por telefone do dono
     */
    List<Animal> findByTelefone(String telefone);

    /**
     * Busca animais por raça
     */
    List<Animal> findByRacaContainingIgnoreCase(String raca);

    /**
     * Busca animais com idade menor ou igual a um valor
     */
    List<Animal> findByIdadeLessThanEqual(Double idade);

    /**
     * Busca animais com idade maior ou igual a um valor
     */
    List<Animal> findByIdadeGreaterThanEqual(Double idade);

    /**
     * Busca animais por tipo (usando coluna discriminadora)
     * Exemplo: buscarPorTipo("CACHORRO"), buscarPorTipo("GATO")
     * 
     * @Query - Define uma query personalizada em JPQL
     */
    @Query("SELECT a FROM Animal a WHERE TYPE(a) = :tipo")
    List<Animal> findByTipo(Class<? extends Animal> tipo);

    /**
     * Conta quantos animais existem de cada espécie
     */
    Long countByEspecie(String especie);

    /**
     * Verifica se existe um animal com determinado nome
     */
    boolean existsByNomeIgnoreCase(String nome);

    /**
     * Busca todos os animais ordenados por nome
     */
    List<Animal> findAllByOrderByNomeAsc();

    /**
     * Busca todos os animais ordenados por idade (mais novos primeiro)
     */
    List<Animal> findAllByOrderByIdadeAsc();

    /**
     * Busca todos os animais ordenados por idade (mais velhos primeiro)
     */
    List<Animal> findAllByOrderByIdadeDesc();
}