package com.example.ATV_Medico_Back.controller;

import com.example.ATV_Medico_Back.dto.MedicoDTO;
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

    // GET: Retorna a lista de médicos
    @GetMapping
    public ResponseEntity<List<MedicoDTO>> listarMedicos() {
        List<MedicoDTO> medicos = medicoService.listarTodos();
        return ResponseEntity.ok(medicos);
    }

    // POST: Cria um novo médico
    @PostMapping
    public ResponseEntity<String> criarMedico(@RequestBody MedicoDTO medicoDTO) {
        try {
            String resultado = medicoService.salvarMedico(medicoDTO);
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PUT: Atualiza um médico existente
    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarMedico(@PathVariable Long id, @RequestBody MedicoDTO medicoDTO) {
        try {
            String resultado = medicoService.atualizarMedico(id, medicoDTO);
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
}
