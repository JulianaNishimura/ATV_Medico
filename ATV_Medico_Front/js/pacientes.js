const baseUrl = "http://localhost:8080/api";

// GET: Carregar pacientes
async function carregarPacientes() {
    try {
        const response = await fetch(`${baseUrl}/pacientes`);
        if (!response.ok) throw new Error("Erro ao buscar pacientes.");
        const pacientes = await response.json();
        const tbody = document.querySelector("#pacientes-table tbody");
        tbody.innerHTML = "";
        pacientes.forEach(paciente => {
            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td>${paciente.id}</td>
                <td>${paciente.nome}</td>
                <td>${paciente.cpf}</td>
                <td>${paciente.idade}</td>
                <td>${paciente.medico.nome} (ID: ${paciente.medico.id})</td>
                <td>
                    <button onclick="editarPaciente(${paciente.id})">✏️</button>
                    <button onclick="deletarPaciente(${paciente.id})">🗑️</button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    } catch (error) {
        console.error("Erro ao carregar pacientes:", error.message);
    }
}

// POST: Cadastrar novo paciente
document.getElementById("paciente-form").addEventListener("submit", async (e) => {
    e.preventDefault();

    const id = document.getElementById("paciente-id").value;
    const nome = document.getElementById("paciente-nome").value;
    const cpf = document.getElementById("paciente-cpf").value;
    const idade = document.getElementById("paciente-idade").value;
    const medicoId = document.getElementById("paciente-medico-id").value;

    const paciente = {
        nome,
        cpf,
        idade,
        medico: { id: medicoId }
    };

    try {
        let method = "POST";
        let url = `${baseUrl}/pacientes`;

        if (id) {
            method = "PUT";
            url += `/${id}`;
        }

        const response = await fetch(url, {
            method,
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(paciente)
        });

        if (!response.ok) throw new Error("Erro ao salvar paciente.");
        document.getElementById("mensagem").textContent = id ? "Paciente atualizado!" : "Paciente cadastrado!";
        document.getElementById("paciente-form").reset();
        carregarPacientes();
    } catch (error) {
        document.getElementById("mensagem").textContent = "Erro: " + error.message;
    }
});

// PUT: Carregar dados para edição
async function editarPaciente(id) {
    try {
        const response = await fetch(`${baseUrl}/pacientes/${id}`);
        if (!response.ok) throw new Error("Erro ao buscar paciente.");
        const paciente = await response.json();

        document.getElementById("paciente-id").value = paciente.id;
        document.getElementById("paciente-nome").value = paciente.nome;
        document.getElementById("paciente-cpf").value = paciente.cpf;
        document.getElementById("paciente-idade").value = paciente.idade;
        document.getElementById("paciente-medico-id").value = paciente.medico.id;
    } catch (error) {
        console.error("Erro ao carregar paciente:", error.message);
    }
}

// DELETE: Remover paciente
async function deletarPaciente(id) {
    if (!confirm("Tem certeza que deseja deletar este paciente?")) return;

    try {
        const response = await fetch(`${baseUrl}/pacientes/${id}`, { method: "DELETE" });
        if (!response.ok) throw new Error("Erro ao deletar paciente.");
        carregarPacientes();
    } catch (error) {
        console.error("Erro ao deletar paciente:", error.message);
    }
}

// Ao carregar a página
window.onload = carregarPacientes;
