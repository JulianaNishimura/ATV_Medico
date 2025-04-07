const baseUrl = "http://localhost:8080/api";

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
            `;
            tbody.appendChild(tr);
        });
    } catch (error) {
        console.error("Erro ao carregar pacientes:", error.message);
    }
}

window.onload = carregarPacientes;
