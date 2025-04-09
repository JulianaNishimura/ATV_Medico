package com.example.ATV_Medico_Back.controller;

import com.example.ATV_Medico_Back.dto.PacienteDTO;
import com.example.ATV_Medico_Back.dto.PacienteComConsultasDTO;
import com.example.ATV_Medico_Back.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    // GET: Retorna a lista de todos os pacientes
    @GetMapping
    public ResponseEntity<List<PacienteDTO>> listarPacientes() {
        List<PacienteDTO> pacientes = pacienteService.listarTodos();
        return ResponseEntity.ok(pacientes);
    }

    // ✅ NOVO ENDPOINT: Lista todos os pacientes com suas consultas
    @GetMapping("/com-consultas")
    public ResponseEntity<List<PacienteComConsultasDTO>> listarPacientesComConsultas() {
        return ResponseEntity.ok(pacienteService.listarPacientesComConsultas());
    }

    // POST: Cria um novo paciente
    @PostMapping
    public ResponseEntity<String> criarPaciente(@RequestBody PacienteDTO pacienteDTO) {
        try {
            String resultado = pacienteService.salvarPaciente(pacienteDTO);
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PUT: Atualiza um paciente existente
    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarPaciente(@PathVariable Long id, @RequestBody PacienteDTO pacienteDTO) {
        try {
            String resultado = pacienteService.atualizarPaciente(id, pacienteDTO);
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE: Remove um paciente
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarPaciente(@PathVariable Long id) {
        try {
            String resultado = pacienteService.deletarPaciente(id);
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
