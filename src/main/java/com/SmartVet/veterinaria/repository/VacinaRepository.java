package com.healthpet.veterinaria.repository;

import com.healthpet.veterinaria.model.Vacina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * VacinaRepository - Interface de Acesso a Dados
 * 
 * Repository para a entidade Vacina.
 * Spring Data JPA cria a implementação automaticamente.
 * 
 * @author Felipe Brito
 * @version 1.0
 */
@Repository
public interface VacinaRepository extends JpaRepository<Vacina, Long> {

    /**
     * Busca todas as vacinas de um animal
     */
    List<Vacina> findByAnimalId(Long animalId);

    /**
     * Busca vacinas de um animal ordenadas por data
     */
    List<Vacina> findByAnimalIdOrderByDataAplicacaoDesc(Long animalId);

    /**
     * Busca vacinas por nome
     */
    List<Vacina> findByNomeContainingIgnoreCase(String nome);

    /**
     * Busca vacinas vencidas (próxima dose passou e não está completa)
     */
    @Query("SELECT v FROM Vacina v WHERE v.proximaDose < :hoje AND v.completa = false")
    List<Vacina> findVacinasVencidas(@Param("hoje") LocalDate hoje);

    /**
     * Busca vacinas próximas ao vencimento (próximos 7 dias)
     */
    @Query("SELECT v FROM Vacina v WHERE v.proximaDose BETWEEN :hoje AND :dataLimite AND v.completa = false")
    List<Vacina> findVacinasProximas(@Param("hoje") LocalDate hoje, @Param("dataLimite") LocalDate dataLimite);

    /**
     * Busca vacinas completas
     */
    List<Vacina> findByCompleta(Boolean completa);

    /**
     * Busca vacinas de um animal que não estão completas
     */
    List<Vacina> findByAnimalIdAndCompleta(Long animalId, Boolean completa);

    /**
     * Conta quantas vacinas um animal tem
     */
    Long countByAnimalId(Long animalId);

    /**
     * Deleta todas as vacinas de um animal
     */
    void deleteByAnimalId(Long animalId);
}