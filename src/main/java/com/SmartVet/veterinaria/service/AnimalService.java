package com.healthpet.veterinaria.service;

import com.healthpet.veterinaria.model.Animal;
import com.healthpet.veterinaria.model.Cachorro;
import com.healthpet.veterinaria.model.Gato;
import com.healthpet.veterinaria.repository.AnimalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * AnimalService - Camada de Serviço (Lógica de Negócio)
 * 
 * Contém toda a lógica de negócio da aplicação.
 * Faz a ponte entre o Controller e o Repository.
 * 
 * @Service - Marca como componente de serviço do Spring
 * @Transactional - Gerencia transações do banco de dados
 * 
 * @author Felipe Brito
 * @version 1.0
 */
@Service
@Transactional
public class AnimalService {

    /**
     * Injeção de Dependência do Repository
     * @Autowired - Spring injeta automaticamente a implementação
     */
    @Autowired
    private AnimalRepository animalRepository;

    // ========== OPERAÇÕES CRUD ==========

    /**
     * CREATE - Cadastra um novo animal
     */
    public Animal cadastrarAnimal(Animal animal) {
        // Valida a idade antes de salvar
        if (!animal.validarIdade()) {
            throw new IllegalArgumentException("Idade inválida: deve estar entre 0 e 50 anos");
        }
        
        // Verifica se já existe animal com o mesmo nome
        if (animalRepository.existsByNomeIgnoreCase(animal.getNome())) {
            throw new IllegalArgumentException("Já existe um animal cadastrado com este nome");
        }
        
        return animalRepository.save(animal);
    }

    /**
     * CREATE - Cadastra um cachorro
     */
    public Cachorro cadastrarCachorro(Cachorro cachorro) {
        if (!cachorro.validarIdade()) {
            throw new IllegalArgumentException("Idade inválida");
        }
        return animalRepository.save(cachorro);
    }

    /**
     * CREATE - Cadastra um gato
     */
    public Gato cadastrarGato(Gato gato) {
        if (!gato.validarIdade()) {
            throw new IllegalArgumentException("Idade inválida");
        }
        return animalRepository.save(gato);
    }

    /**
     * READ - Lista todos os animais
     */
    public List<Animal> listarTodos() {
        return animalRepository.findAll();
    }

    /**
     * READ - Busca animal por ID
     */
    public Optional<Animal> buscarPorId(Long id) {
        return animalRepository.findById(id);
    }

    /**
     * READ - Busca animais por nome (parcial)
     */
    public List<Animal> buscarPorNome(String nome) {
        return animalRepository.findByNomeContainingIgnoreCase(nome);
    }

    /**
     * READ - Busca animal por nome exato
     */
    public Optional<Animal> buscarPorNomeExato(String nome) {
        return animalRepository.findByNomeIgnoreCase(nome);
    }

    /**
     * READ - Busca animais por espécie
     */
    public List<Animal> buscarPorEspecie(String especie) {
        return animalRepository.findByEspecieIgnoreCase(especie);
    }

    /**
     * READ - Busca animais por dono
     */
    public List<Animal> buscarPorDono(String nomeDono) {
        return animalRepository.findByNomeDonoContainingIgnoreCase(nomeDono);
    }

    /**
     * READ - Busca animais por telefone
     */
    public List<Animal> buscarPorTelefone(String telefone) {
        return animalRepository.findByTelefone(telefone);
    }

    /**
     * READ - Lista apenas cachorros
     */
    public List<Animal> listarCachorros() {
        return animalRepository.findByTipo(Cachorro.class);
    }

    /**
     * READ - Lista apenas gatos
     */
    public List<Animal> listarGatos() {
        return animalRepository.findByTipo(Gato.class);
    }

    /**
     * READ - Lista animais ordenados por nome
     */
    public List<Animal> listarOrdenadoPorNome() {
        return animalRepository.findAllByOrderByNomeAsc();
    }

    /**
     * READ - Lista animais ordenados por idade (mais velhos primeiro)
     */
    public List<Animal> listarOrdenadoPorIdade() {
        return animalRepository.findAllByOrderByIdadeDesc();
    }

    /**
     * UPDATE - Atualiza dados de um animal (método legado)
     */
    public Animal atualizarAnimal(Long id, Animal animalAtualizado) {
        Optional<Animal> animalExistente = animalRepository.findById(id);
        
        if (animalExistente.isEmpty()) {
            throw new IllegalArgumentException("Animal não encontrado com ID: " + id);
        }
        
        Animal animal = animalExistente.get();
        
        // Atualiza apenas os campos que não são nulos
        if (animalAtualizado.getNome() != null) {
            animal.setNome(animalAtualizado.getNome());
        }
        if (animalAtualizado.getEspecie() != null) {
            animal.setEspecie(animalAtualizado.getEspecie());
        }
        if (animalAtualizado.getIdade() != null) {
            if (!animalAtualizado.validarIdade()) {
                throw new IllegalArgumentException("Idade inválida");
            }
            animal.setIdade(animalAtualizado.getIdade());
        }
        if (animalAtualizado.getNomeDono() != null) {
            animal.setNomeDono(animalAtualizado.getNomeDono());
        }
        if (animalAtualizado.getTelefone() != null) {
            animal.setTelefone(animalAtualizado.getTelefone());
        }
        if (animalAtualizado.getRaca() != null) {
            animal.setRaca(animalAtualizado.getRaca());
        }
        
        // Se for Cachorro, atualiza porte
        if (animal instanceof Cachorro && animalAtualizado instanceof Cachorro) {
            Cachorro cachorro = (Cachorro) animal;
            Cachorro cachorroAtualizado = (Cachorro) animalAtualizado;
            if (cachorroAtualizado.getPorte() != null) {
                cachorro.setPorte(cachorroAtualizado.getPorte());
            }
        }
        
        // Se for Gato, atualiza pelagem e temperamento
        if (animal instanceof Gato && animalAtualizado instanceof Gato) {
            Gato gato = (Gato) animal;
            Gato gatoAtualizado = (Gato) animalAtualizado;
            if (gatoAtualizado.getPelagem() != null) {
                gato.setPelagem(gatoAtualizado.getPelagem());
            }
            if (gatoAtualizado.getTemperamento() != null) {
                gato.setTemperamento(gatoAtualizado.getTemperamento());
            }
        }
        
        return animalRepository.save(animal);
    }

    /**
     * UPDATE - Atualiza dados de um animal usando Map genérico (NOVO!)
     * Funciona perfeitamente com o frontend que envia JSON
     */
    public Animal atualizarAnimalGenerico(Long id, Map<String, Object> dados) {
        Optional<Animal> animalExistente = animalRepository.findById(id);
        
        if (animalExistente.isEmpty()) {
            throw new IllegalArgumentException("Animal não encontrado com ID: " + id);
        }
        
        Animal animal = animalExistente.get();
        
        // Atualiza campos básicos
        if (dados.containsKey("nome")) {
            animal.setNome((String) dados.get("nome"));
        }
        if (dados.containsKey("especie")) {
            animal.setEspecie((String) dados.get("especie"));
        }
        if (dados.containsKey("idade")) {
            Double idade = ((Number) dados.get("idade")).doubleValue();
            animal.setIdade(idade);
        }
        if (dados.containsKey("nomeDono")) {
            animal.setNomeDono((String) dados.get("nomeDono"));
        }
        if (dados.containsKey("telefone")) {
            animal.setTelefone((String) dados.get("telefone"));
        }
        if (dados.containsKey("raca")) {
            animal.setRaca((String) dados.get("raca"));
        }
        if (dados.containsKey("peso")) {
            animal.setPeso(((Number) dados.get("peso")).doubleValue());
        }
        if (dados.containsKey("altura")) {
            animal.setAltura(((Number) dados.get("altura")).doubleValue());
        }
        if (dados.containsKey("alergias")) {
            animal.setAlergias((String) dados.get("alergias"));
        }
        if (dados.containsKey("medicamentosEmUso")) {
            animal.setMedicamentosEmUso((String) dados.get("medicamentosEmUso"));
        }
        if (dados.containsKey("condicoesPreExistentes")) {
            animal.setCondicoesPreExistentes((String) dados.get("condicoesPreExistentes"));
        }
        
        // Atualiza campos específicos de Cachorro
        if (animal instanceof Cachorro) {
            Cachorro cachorro = (Cachorro) animal;
            if (dados.containsKey("porte")) {
                cachorro.setPorte((String) dados.get("porte"));
            }
        }
        
        // Atualiza campos específicos de Gato
        if (animal instanceof Gato) {
            Gato gato = (Gato) animal;
            if (dados.containsKey("pelagem")) {
                gato.setPelagem((String) dados.get("pelagem"));
            }
            if (dados.containsKey("temperamento")) {
                gato.setTemperamento((String) dados.get("temperamento"));
            }
        }
        
        return animalRepository.save(animal);
    }

    /**
     * DELETE - Remove um animal
     */
    public void removerAnimal(Long id) {
        if (!animalRepository.existsById(id)) {
            throw new IllegalArgumentException("Animal não encontrado com ID: " + id);
        }
        animalRepository.deleteById(id);
    }

    /**
     * Conta total de animais cadastrados
     */
    public long contarTotal() {
        return animalRepository.count();
    }

    /**
     * Conta animais por espécie
     */
    public long contarPorEspecie(String especie) {
        return animalRepository.countByEspecie(especie);
    }

    /**
     * Verifica se existe animal com determinado nome
     */
    public boolean existePorNome(String nome) {
        return animalRepository.existsByNomeIgnoreCase(nome);
    }
}