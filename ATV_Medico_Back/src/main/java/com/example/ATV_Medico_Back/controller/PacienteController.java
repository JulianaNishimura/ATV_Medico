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

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<PacienteComConsultasDTO> buscarPorCpf(@PathVariable String cpf) {
        try {
            return ResponseEntity.ok(pacienteService.buscarPorCpf(cpf));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<PacienteComConsultasDTO> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(pacienteService.buscarPorId(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/com-consultas")
    public ResponseEntity<List<PacienteComConsultasDTO>> listarPacientesComConsultas() {
        try {
            return ResponseEntity.ok(pacienteService.listarPacientesComConsultas());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null); // Retorna status 500 com corpo nulo
        }
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
