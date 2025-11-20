package com.healthpet.veterinaria.controller;

import com.healthpet.veterinaria.model.Prontuario;
import com.healthpet.veterinaria.service.ProntuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * ProntuarioController - Controlador REST API
 * 
 * Endpoints para gerenciar prontuários médicos dos animais.
 * 
 * Endpoints:
 * - GET    /api/prontuarios/animal/{animalId} → Busca prontuário por animal
 * - POST   /api/prontuarios/animal/{animalId} → Cria/atualiza prontuário
 * - DELETE /api/prontuarios/{id}              → Deleta prontuário
 * 
 * @author Felipe Brito
 * @version 1.0
 */
@RestController
@RequestMapping("/api/prontuarios")
@CrossOrigin(origins = "*")
public class ProntuarioController {

    @Autowired
    private ProntuarioService prontuarioService;

    /**
     * GET /api/prontuarios/animal/{animalId}
     * Busca prontuário de um animal específico
     */
    @GetMapping("/animal/{animalId}")
    public ResponseEntity<?> buscarPorAnimal(@PathVariable Long animalId) {
        try {
            Optional<Prontuario> prontuario = prontuarioService.buscarPorAnimalId(animalId);
            
            if (prontuario.isPresent()) {
                return ResponseEntity.ok(prontuario.get());
            } else {
                // Retorna prontuário vazio se não existir
                return ResponseEntity.ok(new Prontuario());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(criarMensagemErro("Erro ao buscar prontuário: " + e.getMessage()));
        }
    }

    /**
     * POST /api/prontuarios/animal/{animalId}
     * Cria ou atualiza prontuário de um animal
     */
    @PostMapping("/animal/{animalId}")
    public ResponseEntity<?> salvarProntuario(
            @PathVariable Long animalId,
            @RequestBody Prontuario prontuario) {
        try {
            Prontuario prontuarioSalvo = prontuarioService.salvarProntuario(animalId, prontuario);
            return ResponseEntity.ok(prontuarioSalvo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(criarMensagemErro(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(criarMensagemErro("Erro ao salvar prontuário: " + e.getMessage()));
        }
    }

    /**
     * DELETE /api/prontuarios/{id}
     * Deleta um prontuário
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarProntuario(@PathVariable Long id) {
        try {
            prontuarioService.deletarProntuario(id);
            return ResponseEntity.ok(criarMensagemSucesso("Prontuário deletado com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(criarMensagemErro("Erro ao deletar prontuário: " + e.getMessage()));
        }
    }

    /**
     * GET /api/prontuarios/{id}
     * Busca prontuário por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            Optional<Prontuario> prontuario = prontuarioService.buscarPorId(id);
            
            if (prontuario.isPresent()) {
                return ResponseEntity.ok(prontuario.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(criarMensagemErro("Prontuário não encontrado"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(criarMensagemErro("Erro ao buscar prontuário: " + e.getMessage()));
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