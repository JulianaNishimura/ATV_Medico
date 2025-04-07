const baseUrl = "http://localhost:8080/api/medicos";

async function carregarMedicos() {
    try {
        const response = await fetch(baseUrl);
        if (!response.ok) throw new Error("Erro ao buscar médicos.");
        const medicos = await response.json();
        const tbody = document.querySelector("#medicos-table tbody");
        tbody.innerHTML = "";

        medicos.forEach(medico => {
            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td>${medico.id}</td>
                <td>${medico.nome}</td>
                <td>${medico.crm}</td>
                <td>${medico.especialidade}</td>
                <td>${medico.numeroConsultorio}</td>
                <td>
                    <button onclick="editarMedico(${medico.id})">✏️</button>
                    <button onclick="excluirMedico(${medico.id})">🗑️</button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    } catch (err) {
        alert(err.message);
    }
}

async function adicionarMedico(e) {
    e.preventDefault();
    const medico = {
        nome: document.getElementById("nome").value,
        crm: document.getElementById("crm").value,
        especialidade: document.getElementById("especialidade").value,
        numeroConsultorio: document.getElementById("consultorio").value
    };

    const response = await fetch(baseUrl, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(medico)
    });

    if (response.ok) {
        alert("Médico adicionado!");
        carregarMedicos();
        document.getElementById("form-medico").reset();
    } else {
        alert("Erro ao adicionar médico.");
    }
}

async function editarMedico(id) {
    const response = await fetch(`${baseUrl}/${id}`);
    const medico = await response.json();

    document.getElementById("medico-id").value = medico.id;
    document.getElementById("nome").value = medico.nome;
    document.getElementById("crm").value = medico.crm;
    document.getElementById("especialidade").value = medico.especialidade;
    document.getElementById("consultorio").value = medico.numeroConsultorio;

    document.getElementById("btn-salvar").textContent = "Atualizar";
}

async function atualizarMedico(e) {
    e.preventDefault();

    const id = document.getElementById("medico-id").value;
    if (!id) return adicionarMedico(e); // Se não tiver ID, é um POST

    const medico = {
        nome: document.getElementById("nome").value,
        crm: document.getElementById("crm").value,
        especialidade: document.getElementById("especialidade").value,
        numeroConsultorio: document.getElementById("consultorio").value
    };

    const response = await fetch(`${baseUrl}/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(medico)
    });

    if (response.ok) {
        alert("Médico atualizado!");
        carregarMedicos();
        document.getElementById("form-medico").reset();
        document.getElementById("btn-salvar").textContent = "Salvar";
    } else {
        alert("Erro ao atualizar médico.");
    }
}

async function excluirMedico(id) {
    if (!confirm("Deseja realmente excluir este médico?")) return;
    const response = await fetch(`${baseUrl}/${id}`, {
        method: "DELETE"
    });

    if (response.ok) {
        alert("Médico excluído.");
        carregarMedicos();
    } else {
        alert("Erro ao excluir médico.");
    }
}

document.getElementById("form-medico").addEventListener("submit", atualizarMedico);

window.onload = carregarMedicos;
