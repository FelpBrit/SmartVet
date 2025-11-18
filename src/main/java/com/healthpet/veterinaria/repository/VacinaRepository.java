package com.healthpet.veterinaria.repository;

import com.healthpet.veterinaria.model.Animal;
import com.healthpet.veterinaria.model.Vacina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * VacinaRepository - Interface de Acesso a Dados de Vacinas
 * 
 * Fornece métodos para consultar vacinas no banco de dados
 * 
 * @author Felipe Brito
 * @version 1.0
 */
@Repository
public interface VacinaRepository extends JpaRepository<Vacina, Long> {

    /**
     * Busca todas as vacinas de um animal específico
     */
    List<Vacina> findByAnimal(Animal animal);

    /**
     * Busca todas as vacinas de um animal por ID
     */
    List<Vacina> findByAnimalId(Long animalId);

    /**
     * Busca vacinas por nome
     */
    List<Vacina> findByNomeVacinaContainingIgnoreCase(String nomeVacina);

    /**
     * Busca vacinas aplicadas em uma data específica
     */
    List<Vacina> findByDataAplicacao(LocalDate dataAplicacao);

    /**
     * Busca vacinas com próxima dose em uma data específica
     */
    List<Vacina> findByProximaDose(LocalDate proximaDose);

    /**
     * Busca vacinas com próxima dose antes de uma data (vencidas/vencendo)
     */
    List<Vacina> findByProximaDoseBefore(LocalDate data);

    /**
     * Busca vacinas com próxima dose entre duas datas
     */
    List<Vacina> findByProximaDoseBetween(LocalDate dataInicio, LocalDate dataFim);

    /**
     * Busca vacinas por status
     */
    List<Vacina> findByStatus(Vacina.StatusVacina status);

    /**
     * Busca vacinas de um animal ordenadas por data de aplicação
     */
    List<Vacina> findByAnimalIdOrderByDataAplicacaoDesc(Long animalId);

    /**
     * Conta quantas vacinas um animal tem
     */
    Long countByAnimalId(Long animalId);

    /**
     * Busca vacinas vencidas (próxima dose antes de hoje)
     * Query personalizada em JPQL
     */
    @Query("SELECT v FROM Vacina v WHERE v.proximaDose < CURRENT_DATE")
    List<Vacina> findVacinasVencidas();

    /**
     * Busca vacinas que vencem nos próximos X dias
     */
    @Query("SELECT v FROM Vacina v WHERE v.proximaDose BETWEEN CURRENT_DATE AND :dataLimite")
    List<Vacina> findVacinasVencendoAte(LocalDate dataLimite);

    /**
     * Busca última vacina de um tipo para um animal
     */
    @Query("SELECT v FROM Vacina v WHERE v.animal.id = :animalId AND v.nomeVacina = :nomeVacina ORDER BY v.dataAplicacao DESC")
    List<Vacina> findUltimaVacinaPorTipo(Long animalId, String nomeVacina);
}