package com.example.ATV_Medico_Back.controller;

import com.example.ATV_Medico_Back.dto.MedicoDTO;
import com.example.ATV_Medico_Back.dto.MedicoComConsultasDTO;
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

    // GET: Lista todos os médicos (dados básicos)
    @GetMapping
    public ResponseEntity<List<MedicoDTO>> listarMedicos() {
        return ResponseEntity.ok(medicoService.listarTodos());
    }

    // GET: Lista todos os médicos com consultas e pacientes
    @GetMapping("/com-consultas")
    public ResponseEntity<List<MedicoComConsultasDTO>> listarMedicosComConsultas() {
        return ResponseEntity.ok(medicoService.listarMedicosComConsultas());
    }

    // POST: Cria um novo médico
    @PostMapping
    public ResponseEntity<String> criarMedico(@RequestBody MedicoDTO medicoDTO) {
        try {
            return ResponseEntity.ok(medicoService.salvarMedico(medicoDTO));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PUT: Atualiza um médico existente
    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarMedico(@PathVariable Long id, @RequestBody MedicoDTO medicoDTO) {
        try {
            return ResponseEntity.ok(medicoService.atualizarMedico(id, medicoDTO));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE: Remove um médico
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarMedico(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(medicoService.deletarMedico(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // POST: Adiciona um paciente ao médico
    @PostMapping("/{id}/pacientes")
    public ResponseEntity<String> adicionarPaciente(@PathVariable Long id, @RequestParam Long pacienteId) {
        try {
            return ResponseEntity.ok(medicoService.adicionarPaciente(id, pacienteId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
