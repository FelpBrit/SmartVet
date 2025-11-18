package com.healthpet.veterinaria.controller;

import com.healthpet.veterinaria.model.Animal;
import com.healthpet.veterinaria.model.Cachorro;
import com.healthpet.veterinaria.model.Gato;
import com.healthpet.veterinaria.service.AnimalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * AnimalController - Controlador REST API
 * 
 * Define os endpoints da API REST para gerenciar animais.
 * ATUALIZADO: Suporte completo para edição de Cachorro e Gato
 * 
 * @author Felipe Brito
 * @version 2.0
 */
@RestController
@RequestMapping("/api/animais")
@CrossOrigin(origins = "*")
public class AnimalController {

    @Autowired
    private AnimalService animalService;

    // ========== ENDPOINTS GET (READ) ==========

    @GetMapping
    public ResponseEntity<List<Animal>> listarTodos() {
        List<Animal> animais = animalService.listarTodos();
        return ResponseEntity.ok(animais);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            Optional<Animal> animal = animalService.buscarPorId(id);
            
            if (animal.isPresent()) {
                return ResponseEntity.ok(animal.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(criarMensagemErro("Animal não encontrado com ID: " + id));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(criarMensagemErro("Erro ao buscar animal: " + e.getMessage()));
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Animal>> buscarPorNome(@RequestParam String nome) {
        List<Animal> animais = animalService.buscarPorNome(nome);
        return ResponseEntity.ok(animais);
    }

    @GetMapping("/especie/{especie}")
    public ResponseEntity<List<Animal>> buscarPorEspecie(@PathVariable String especie) {
        List<Animal> animais = animalService.buscarPorEspecie(especie);
        return ResponseEntity.ok(animais);
    }

    @GetMapping("/dono")
    public ResponseEntity<List<Animal>> buscarPorDono(@RequestParam String nome) {
        List<Animal> animais = animalService.buscarPorDono(nome);
        return ResponseEntity.ok(animais);
    }

    @GetMapping("/cachorros")
    public ResponseEntity<List<Animal>> listarCachorros() {
        List<Animal> cachorros = animalService.listarCachorros();
        return ResponseEntity.ok(cachorros);
    }

    @GetMapping("/gatos")
    public ResponseEntity<List<Animal>> listarGatos() {
        List<Animal> gatos = animalService.listarGatos();
        return ResponseEntity.ok(gatos);
    }

    @GetMapping("/estatisticas")
    public ResponseEntity<Map<String, Object>> obterEstatisticas() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", animalService.contarTotal());
        stats.put("cachorros", animalService.listarCachorros().size());
        stats.put("gatos", animalService.listarGatos().size());
        return ResponseEntity.ok(stats);
    }

    // ========== ENDPOINTS POST (CREATE) ==========

    @PostMapping
    public ResponseEntity<?> cadastrarAnimal(@Valid @RequestBody Animal animal) {
        try {
            Animal animalSalvo = animalService.cadastrarAnimal(animal);
            return ResponseEntity.status(HttpStatus.CREATED).body(animalSalvo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(criarMensagemErro(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(criarMensagemErro("Erro ao cadastrar animal: " + e.getMessage()));
        }
    }

    @PostMapping("/cachorro")
    public ResponseEntity<?> cadastrarCachorro(@Valid @RequestBody Cachorro cachorro) {
        try {
            Cachorro cachorroSalvo = animalService.cadastrarCachorro(cachorro);
            return ResponseEntity.status(HttpStatus.CREATED).body(cachorroSalvo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(criarMensagemErro(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(criarMensagemErro("Erro ao cadastrar cachorro: " + e.getMessage()));
        }
    }

    @PostMapping("/gato")
    public ResponseEntity<?> cadastrarGato(@Valid @RequestBody Gato gato) {
        try {
            Gato gatoSalvo = animalService.cadastrarGato(gato);
            return ResponseEntity.status(HttpStatus.CREATED).body(gatoSalvo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(criarMensagemErro(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(criarMensagemErro("Erro ao cadastrar gato: " + e.getMessage()));
        }
    }

    // ========== ENDPOINTS PUT (UPDATE) - CORRIGIDO! ==========

    /**
     * PUT /api/animais/{id}
     * Atualiza dados de um animal (funciona para Animal, Cachorro e Gato)
     * Agora aceita Map genérico e detecta o tipo automaticamente
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarAnimal(
            @PathVariable Long id,
            @RequestBody Map<String, Object> dadosAtualizados) {
        try {
            Animal animal = animalService.atualizarAnimalGenerico(id, dadosAtualizados);
            return ResponseEntity.ok(animal);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(criarMensagemErro(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(criarMensagemErro("Erro ao atualizar animal: " + e.getMessage()));
        }
    }

    // ========== ENDPOINTS DELETE (DELETE) ==========

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removerAnimal(@PathVariable Long id) {
        try {
            animalService.removerAnimal(id);
            return ResponseEntity.ok(criarMensagemSucesso("Animal removido com sucesso!"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(criarMensagemErro(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(criarMensagemErro("Erro ao remover animal: " + e.getMessage()));
        }
    }

    // ========== MÉTODOS AUXILIARES ==========

    private Map<String, String> criarMensagemErro(String mensagem) {
        Map<String, String> response = new HashMap<>();
        response.put("erro", mensagem);
        response.put("sucesso", "false");
        return response;
    }

    private Map<String, String> criarMensagemSucesso(String mensagem) {
        Map<String, String> response = new HashMap<>();
        response.put("mensagem", mensagem);
        response.put("sucesso", "true");
        return response;
    }
}