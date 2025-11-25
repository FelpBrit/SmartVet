package com.healthpet.veterinaria.repository;

import com.healthpet.veterinaria.model.Prontuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * ProntuarioRepository - Interface de Acesso a Dados
 * 
 * Repository para a entidade Prontuario.
 * Spring Data JPA cria a implementação automaticamente.
 * 
 * @author Felipe Brito
 * @version 1.0
 */
@Repository
public interface ProntuarioRepository extends JpaRepository <Prontuario, Long> {

    /**
     * Busca prontuário por ID do animal
     */
    Optional<Prontuario> findByAnimalId(Long animalId);

    /**
     * Verifica se existe prontuário para um animal
     */
    boolean existsByAnimalId(Long animalId);

    /**
     * Deleta prontuário por ID do animal
     */
    void deleteByAnimalId(Long animalId);
}