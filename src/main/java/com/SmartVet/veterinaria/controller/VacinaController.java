package com.healthpet.veterinaria.controller;

import com.healthpet.veterinaria.model.Vacina;
import com.healthpet.veterinaria.service.VacinaService;
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
 * VacinaController - Controlador REST API
 * 
 * Endpoints para gerenciar vacinas dos animais.
 * 
 * Endpoints:
 * - GET    /api/vacinas                    → Lista todas
 * - GET    /api/vacinas/{id}               → Busca por ID
 * - GET    /api/vacinas/animal/{animalId}  → Lista vacinas de um animal
 * - POST   /api/vacinas/animal/{animalId}  → Registra vacina
 * - PUT    /api/vacinas/{id}               → Atualiza vacina
 * - DELETE /api/vacinas/{id}               → Deleta vacina
 * - GET    /api/vacinas/vencidas           → Lista vencidas
 * - GET    /api/vacinas/proximas           → Lista próximas
 * - PUT    /api/vacinas/{id}/completa      → Marca como completa
 * 
 * @author Felipe Brito
 * @version 1.0
 */
@RestController
@RequestMapping("/api/vacinas")
@CrossOrigin(origins = "*")
public class VacinaController {

    @Autowired
    private VacinaService vacinaService;

    /**
     * GET /api/vacinas
     * Lista todas as vacinas
     */
    @GetMapping
    public ResponseEntity<List<Vacina>> listarTodas() {
        List<Vacina> vacinas = vacinaService.listarTodas();
        return ResponseEntity.ok(vacinas);
    }

    /**
     * GET /api/vacinas/{id}
     * Busca vacina por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            Optional<Vacina> vacina = vacinaService.buscarPorId(id);
            
            if (vacina.isPresent()) {
                return ResponseEntity.ok(vacina.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(criarMensagemErro("Vacina não encontrada"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(criarMensagemErro("Erro ao buscar vacina: " + e.getMessage()));
        }
    }

    /**
     * GET /api/vacinas/animal/{animalId}
     * Lista todas as vacinas de um animal
     */
    @GetMapping("/animal/{animalId}")
    public ResponseEntity<List<Vacina>> listarPorAnimal(@PathVariable Long animalId) {
        List<Vacina> vacinas = vacinaService.listarVacinasPorAnimal(animalId);
        return ResponseEntity.ok(vacinas);
    }

    /**
     * POST /api/vacinas/animal/{animalId}
     * Registra uma nova vacina para um animal
     */
    @PostMapping("/animal/{animalId}")
    public ResponseEntity<?> registrarVacina(
            @PathVariable Long animalId,
            @Valid @RequestBody Vacina vacina) {
        try {
            Vacina vacinaSalva = vacinaService.registrarVacina(animalId, vacina);
            return ResponseEntity.status(HttpStatus.CREATED).body(vacinaSalva);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(criarMensagemErro(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(criarMensagemErro("Erro ao registrar vacina: " + e.getMessage()));
        }
    }

    /**
     * PUT /api/vacinas/{id}
     * Atualiza uma vacina
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarVacina(
            @PathVariable Long id,
            @RequestBody Vacina vacina) {
        try {
            Vacina vacinaAtualizada = vacinaService.atualizarVacina(id, vacina);
            return ResponseEntity.ok(vacinaAtualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(criarMensagemErro(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(criarMensagemErro("Erro ao atualizar vacina: " + e.getMessage()));
        }
    }

    /**
     * DELETE /api/vacinas/{id}
     * Deleta uma vacina
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarVacina(@PathVariable Long id) {
        try {
            vacinaService.deletarVacina(id);
            return ResponseEntity.ok(criarMensagemSucesso("Vacina deletada com sucesso"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(criarMensagemErro(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(criarMensagemErro("Erro ao deletar vacina: " + e.getMessage()));
        }
    }

    /**
     * GET /api/vacinas/vencidas
     * Lista todas as vacinas vencidas
     */
    @GetMapping("/vencidas")
    public ResponseEntity<List<Vacina>> listarVencidas() {
        List<Vacina> vacinas = vacinaService.buscarVacinasVencidas();
        return ResponseEntity.ok(vacinas);
    }

    /**
     * GET /api/vacinas/proximas
     * Lista vacinas próximas ao vencimento (próximos 7 dias)
     */
    @GetMapping("/proximas")
    public ResponseEntity<List<Vacina>> listarProximas() {
        List<Vacina> vacinas = vacinaService.buscarVacinasProximas();
        return ResponseEntity.ok(vacinas);
    }

    /**
     * GET /api/vacinas/animal/{animalId}/pendentes
     * Lista vacinas pendentes de um animal
     */
    @GetMapping("/animal/{animalId}/pendentes")
    public ResponseEntity<List<Vacina>> listarPendentes(@PathVariable Long animalId) {
        List<Vacina> vacinas = vacinaService.buscarVacinasPendentes(animalId);
        return ResponseEntity.ok(vacinas);
    }

    /**
     * PUT /api/vacinas/{id}/completa
     * Marca uma vacina como completa
     */
    @PutMapping("/{id}/completa")
    public ResponseEntity<?> marcarComoCompleta(@PathVariable Long id) {
        try {
            Vacina vacina = vacinaService.marcarComoCompleta(id);
            return ResponseEntity.ok(vacina);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(criarMensagemErro(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(criarMensagemErro("Erro ao marcar vacina como completa: " + e.getMessage()));
        }
    }

    /**
     * GET /api/vacinas/animal/{animalId}/contagem
     * Conta quantas vacinas um animal tem
     */
    @GetMapping("/animal/{animalId}/contagem")
    public ResponseEntity<Map<String, Long>> contarVacinas(@PathVariable Long animalId) {
        Long quantidade = vacinaService.contarVacinasDoAnimal(animalId);
        Map<String, Long> response = new HashMap<>();
        response.put("quantidade", quantidade);
        return ResponseEntity.ok(response);
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