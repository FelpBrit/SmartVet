package com.healthpet.veterinaria.service;

import com.healthpet.veterinaria.model.Animal;
import com.healthpet.veterinaria.model.Prontuario;
import com.healthpet.veterinaria.repository.AnimalRepository;
import com.healthpet.veterinaria.repository.ProntuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * ProntuarioService - Camada de Serviço
 * 
 * Contém a lógica de negócio para gerenciar prontuários.
 * 
 * @author Felipe Brito
 * @version 1.0
 */
@Service
@Transactional
public class ProntuarioService {

    @Autowired
    private ProntuarioRepository prontuarioRepository;

    @Autowired
    private AnimalRepository animalRepository;

    /**
     * Cria ou atualiza prontuário de um animal
     */
    public Prontuario salvarProntuario(Long animalId, Prontuario prontuario) {
        // Busca o animal
        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new IllegalArgumentException("Animal não encontrado"));

        // Verifica se já existe prontuário
        Optional<Prontuario> prontuarioExistente = prontuarioRepository.findByAnimalId(animalId);

        if (prontuarioExistente.isPresent()) {
            // Atualiza o prontuário existente
            Prontuario p = prontuarioExistente.get();
            p.setPeso(prontuario.getPeso());
            p.setAltura(prontuario.getAltura());
            p.setAlergias(prontuario.getAlergias());
            p.setMedicamentosEmUso(prontuario.getMedicamentosEmUso());
            p.setCondicoesPreExistentes(prontuario.getCondicoesPreExistentes());
            p.setObservacoes(prontuario.getObservacoes());
            return prontuarioRepository.save(p);
        } else {
            // Cria novo prontuário
            prontuario.setAnimal(animal);
            return prontuarioRepository.save(prontuario);
        }
    }

    /**
     * Busca prontuário por ID do animal
     */
    public Optional<Prontuario> buscarPorAnimalId(Long animalId) {
        return prontuarioRepository.findByAnimalId(animalId);
    }

    /**
     * Busca prontuário por ID
     */
    public Optional<Prontuario> buscarPorId(Long id) {
        return prontuarioRepository.findById(id);
    }

    /**
     * Verifica se animal tem prontuário
     */
    public boolean animalTemProntuario(Long animalId) {
        return prontuarioRepository.existsByAnimalId(animalId);
    }

    /**
     * Deleta prontuário
     */
    public void deletarProntuario(Long id) {
        prontuarioRepository.deleteById(id);
    }

    /**
     * Deleta prontuário por ID do animal
     */
    public void deletarPorAnimalId(Long animalId) {
        prontuarioRepository.deleteByAnimalId(animalId);
    }
}
