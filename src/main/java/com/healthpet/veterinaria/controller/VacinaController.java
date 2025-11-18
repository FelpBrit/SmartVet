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
 * VacinaController - Controlador REST API para Vacinas
 * 
 * Endpoints:
 * - GET    /api/vacinas              → Lista todas
 * - GET    /api/vacinas/{id}         → Busca por ID
 * - GET    /api/vacinas/animal/{id}  → Busca vacinas de um animal
 * - GET    /api/vacinas/vencidas     → Busca vacinas vencidas
 * - GET    /api/vacinas/alertas/{id} → Alertas de um animal
 * - POST   /api/vacinas              → Registra vacina
 * - PUT    /api/vacinas/{id}         → Atualiza vacina
 * - DELETE /api/vacinas/{id}         → Remove vacina
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

    // ========== ENDPOINTS GET (READ) ==========

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
     * Busca uma vacina por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            Optional<Vacina> vacina = vacinaService.buscarPorId(id);
            
            if (vacina.isPresent()) {
                return ResponseEntity.ok(vacina.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(criarMensagemErro("Vacina não encontrada com ID: " + id));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(criarMensagemErro("Erro ao buscar vacina: " + e.getMessage()));
        }
    }

    /**
     * GET /api/vacinas/animal/{animalId}
     * Busca todas as vacinas de um animal
     */
    @GetMapping("/animal/{animalId}")
    public ResponseEntity<List<Vacina>> buscarPorAnimal(@PathVariable Long animalId) {
        List<Vacina> vacinas = vacinaService.buscarPorAnimal(animalId);
        return ResponseEntity.ok(vacinas);
    }

    /**
     * GET /api/vacinas/vencidas
     * Lista todas as vacinas vencidas
     */
    @GetMapping("/vencidas")
    public ResponseEntity<List<Vacina>> buscarVencidas() {
        List<Vacina> vacinas = vacinaService.buscarVacinasVencidas();
        return ResponseEntity.ok(vacinas);
    }

    /**
     * GET /api/vacinas/vencendo?dias=30
     * Busca vacinas que vencem nos próximos X dias
     */
    @GetMapping("/vencendo")
    public ResponseEntity<List<Vacina>> buscarVencendoEm(@RequestParam(defaultValue = "30") int dias) {
        List<Vacina> vacinas = vacinaService.buscarVacinasVencendoEm(dias);
        return ResponseEntity.ok(vacinas);
    }

    /**
     * GET /api/vacinas/alertas/{animalId}
     * Retorna alertas de vacinas para um animal
     */
    @GetMapping("/alertas/{animalId}")
    public ResponseEntity<List<String>> getAlertasVacinas(@PathVariable Long animalId) {
        List<String> alertas = vacinaService.getAlertasVacinas(animalId);
        return ResponseEntity.ok(alertas);
    }

    /**
     * GET /api/vacinas/buscar?nome=antirrábica
     * Busca vacinas por nome
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<Vacina>> buscarPorNome(@RequestParam String nome) {
        List<Vacina> vacinas = vacinaService.buscarPorNome(nome);
        return ResponseEntity.ok(vacinas);
    }

    /**
     * GET /api/vacinas/proxima/{animalId}
     * Retorna próxima vacina a vencer de um animal
     */
    @GetMapping("/proxima/{animalId}")
    public ResponseEntity<?> getProximaVacina(@PathVariable Long animalId) {
        Optional<Vacina> vacina = vacinaService.getProximaVacinaAVencer(animalId);
        
        if (vacina.isPresent()) {
            return ResponseEntity.ok(vacina.get());
        } else {
            return ResponseEntity.ok(criarMensagemSucesso("Nenhuma vacina pendente"));
        }
    }

    /**
     * GET /api/vacinas/estatisticas/{animalId}
     * Retorna estatísticas de vacinas de um animal
     */
    @GetMapping("/estatisticas/{animalId}")
    public ResponseEntity<Map<String, Object>> getEstatisticas(@PathVariable Long animalId) {
        Map<String, Object> stats = new HashMap<>();
        
        List<Vacina> todasVacinas = vacinaService.buscarPorAnimal(animalId);
        long vencidas = todasVacinas.stream().filter(Vacina::isVencida).count();
        long proximasVencer = todasVacinas.stream().filter(Vacina::isProximaVencer).count();
        
        stats.put("total", todasVacinas.size());
        stats.put("vencidas", vencidas);
        stats.put("proximasVencer", proximasVencer);
        stats.put("emDia", todasVacinas.size() - vencidas - proximasVencer);
        
        return ResponseEntity.ok(stats);
    }

    // ========== ENDPOINTS POST (CREATE) ==========

    /**
     * POST /api/vacinas
     * Registra uma nova vacina
     * 
     * Body JSON exemplo:
     * {
     *   "animal": { "id": 1 },
     *   "nomeVacina": "V10",
     *   "dataAplicacao": "2024-01-15",
     *   "proximaDose": "2024-02-15",
     *   "dose": "1ª dose",
     *   "veterinarioResponsavel": "Dr. Carlos Silva",
     *   "lote": "ABC123",
     *   "observacoes": "Animal reagiu bem"
     * }
     */
    @PostMapping
    public ResponseEntity<?> registrarVacina(@Valid @RequestBody Vacina vacina) {
        try {
            Vacina vacinaSalva = vacinaService.registrarVacina(vacina);
            return ResponseEntity.status(HttpStatus.CREATED).body(vacinaSalva);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(criarMensagemErro(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(criarMensagemErro("Erro ao registrar vacina: " + e.getMessage()));
        }
    }

    // ========== ENDPOINTS PUT (UPDATE) ==========

    /**
     * PUT /api/vacinas/{id}
     * Atualiza dados de uma vacina
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarVacina(
            @PathVariable Long id,
            @RequestBody Vacina vacinaAtualizada) {
        try {
            Vacina vacina = vacinaService.atualizarVacina(id, vacinaAtualizada);
            return ResponseEntity.ok(vacina);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(criarMensagemErro(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(criarMensagemErro("Erro ao atualizar vacina: " + e.getMessage()));
        }
    }

    // ========== ENDPOINTS DELETE (DELETE) ==========

    /**
     * DELETE /api/vacinas/{id}
     * Remove uma vacina
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removerVacina(@PathVariable Long id) {
        try {
            vacinaService.removerVacina(id);
            return ResponseEntity.ok(criarMensagemSucesso("Vacina removida com sucesso!"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(criarMensagemErro(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(criarMensagemErro("Erro ao remover vacina: " + e.getMessage()));
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