package com.example.ATV_Medico_Back.controller;
import com.example.ATV_Medico_Back.model.Medico;
import com.example.ATV_Medico_Back.service.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicos")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    // GET: Retorna a lista de médicos (único endpoint que retorna List<Medico>)
    @GetMapping
    public ResponseEntity<List<Medico>> listarMedicos() {
        List<Medico> medicos = medicoService.listarTodos();
        return ResponseEntity.ok(medicos);
    }

    // POST: Cria um novo médico
    @PostMapping
    public ResponseEntity<String> criarMedico(@RequestBody Medico medico) {
        try {
            String resultado = medicoService.salvarMedico(medico);
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PUT: Atualiza um médico existente
    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarMedico(@PathVariable Long id, @RequestBody Medico medico) {
        try {
            String resultado = medicoService.atualizarMedico(id, medico);
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE: Remove um médico
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarMedico(@PathVariable Long id) {
        try {
            String resultado = medicoService.deletarMedico(id);
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // POST: Adiciona um paciente ao médico
    @PostMapping("/{id}/pacientes")
    public ResponseEntity<String> adicionarPaciente(@PathVariable Long id, @RequestParam Long pacienteId) {
        try {
            String resultado = medicoService.adicionarPaciente(id, pacienteId);
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // POST: Adiciona uma consulta ao médico
    @PostMapping("/{id}/consultas")
    public ResponseEntity<String> adicionarConsulta(@PathVariable Long id, @RequestBody Consulta consulta) {
        try {
            String resultado = medicoService.adicionarConsulta(id, consulta);
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
