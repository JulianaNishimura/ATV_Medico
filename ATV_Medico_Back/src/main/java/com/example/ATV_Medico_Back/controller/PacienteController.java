package com.example.ATV_Medico_Back.controller;

import com.example.ATV_Medico_Back.model.Paciente;
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
    public ResponseEntity<List<Paciente>> listarPacientes() {
        List<Paciente> pacientes = pacienteService.listarTodos();
        return ResponseEntity.ok(pacientes);
    }

    // POST: Cria um novo paciente
    @PostMapping
    public ResponseEntity<String> criarPaciente(@RequestBody Paciente paciente) {
        try {
            String resultado = pacienteService.salvarPaciente(paciente);
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PUT: Atualiza um paciente existente
    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarPaciente(@PathVariable Long id, @RequestBody Paciente paciente) {
        try {
            String resultado = pacienteService.atualizarPaciente(id, paciente);
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