package com.healthpet.veterinaria.service;

import com.healthpet.veterinaria.model.Animal;
import com.healthpet.veterinaria.model.Vacina;
import com.healthpet.veterinaria.repository.AnimalRepository;
import com.healthpet.veterinaria.repository.VacinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * VacinaService - Camada de Serviço
 * 
 * Contém a lógica de negócio para gerenciar vacinas.
 * 
 * @author Felipe Brito
 * @version 1.0
 */
@Service
@Transactional
public class VacinaService {

    @Autowired
    private VacinaRepository vacinaRepository;

    @Autowired
    private AnimalRepository animalRepository;

    /**
     * Registra uma nova vacina para um animal
     */
    public Vacina registrarVacina(Long animalId, Vacina vacina) {
        // Busca o animal
        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new IllegalArgumentException("Animal não encontrado"));

        // Associa a vacina ao animal
        vacina.setAnimal(animal);
        
        return vacinaRepository.save(vacina);
    }

    /**
     * Lista todas as vacinas de um animal
     */
    public List<Vacina> listarVacinasPorAnimal(Long animalId) {
        return vacinaRepository.findByAnimalIdOrderByDataAplicacaoDesc(animalId);
    }

    /**
     * Busca vacina por ID
     */
    public Optional<Vacina> buscarPorId(Long id) {
        return vacinaRepository.findById(id);
    }

    /**
     * Atualiza uma vacina
     */
    public Vacina atualizarVacina(Long id, Vacina vacinaAtualizada) {
        Vacina vacina = vacinaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vacina não encontrada"));

        // Atualiza os campos
        if (vacinaAtualizada.getNome() != null) {
            vacina.setNome(vacinaAtualizada.getNome());
        }
        if (vacinaAtualizada.getDataAplicacao() != null) {
            vacina.setDataAplicacao(vacinaAtualizada.getDataAplicacao());
        }
        if (vacinaAtualizada.getProximaDose() != null) {
            vacina.setProximaDose(vacinaAtualizada.getProximaDose());
        }
        if (vacinaAtualizada.getLote() != null) {
            vacina.setLote(vacinaAtualizada.getLote());
        }
        if (vacinaAtualizada.getVeterinario() != null) {
            vacina.setVeterinario(vacinaAtualizada.getVeterinario());
        }
        if (vacinaAtualizada.getObservacoes() != null) {
            vacina.setObservacoes(vacinaAtualizada.getObservacoes());
        }
        if (vacinaAtualizada.getCompleta() != null) {
            vacina.setCompleta(vacinaAtualizada.getCompleta());
        }

        return vacinaRepository.save(vacina);
    }

    /**
     * Deleta uma vacina
     */
    public void deletarVacina(Long id) {
        if (!vacinaRepository.existsById(id)) {
            throw new IllegalArgumentException("Vacina não encontrada");
        }
        vacinaRepository.deleteById(id);
    }

    /**
     * Lista todas as vacinas
     */
    public List<Vacina> listarTodas() {
        return vacinaRepository.findAll();
    }

    /**
     * Busca vacinas vencidas
     */
    public List<Vacina> buscarVacinasVencidas() {
        return vacinaRepository.findVacinasVencidas(LocalDate.now());
    }

    /**
     * Busca vacinas próximas ao vencimento (próximos 7 dias)
     */
    public List<Vacina> buscarVacinasProximas() {
        LocalDate hoje = LocalDate.now();
        LocalDate dataLimite = hoje.plusDays(7);
        return vacinaRepository.findVacinasProximas(hoje, dataLimite);
    }

    /**
     * Busca vacinas pendentes de um animal
     */
    public List<Vacina> buscarVacinasPendentes(Long animalId) {
        return vacinaRepository.findByAnimalIdAndCompleta(animalId, false);
    }

    /**
     * Conta quantas vacinas um animal tem
     */
    public Long contarVacinasDoAnimal(Long animalId) {
        return vacinaRepository.countByAnimalId(animalId);
    }

    /**
     * Marca vacina como completa
     */
    public Vacina marcarComoCompleta(Long id) {
        Vacina vacina = vacinaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vacina não encontrada"));
        
        vacina.setCompleta(true);
        vacina.setProximaDose(null); // Remove próxima dose se completa
        
        return vacinaRepository.save(vacina);
    }
}