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
                <td>${medico.salario ? medico.salario.toFixed(2) : '0.00'}</td>
                <td>
                    <button onclick="editarMedico(${medico.id}, '${medico.nome}', '${medico.crm}', '${medico.especialidade}', '${medico.numeroConsultorio}', ${medico.salario})">✏️Editar</button>
                    <button onclick="excluirMedico(${medico.id})">🗑️Excluir</button>
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
        numeroConsultorio: document.getElementById("consultorio").value,
        salario: parseFloat(document.getElementById("salario").value) || 0
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
        resetForm();
    } else {
        alert("Erro ao adicionar médico.");
    }
}

function editarMedico(id, nome, crm, especialidade, numeroConsultorio, salario) {
    // Preenche os campos com os dados da tabela
    document.getElementById("medico-id").value = id;
    document.getElementById("nome").value = nome;
    document.getElementById("crm").value = crm;
    document.getElementById("especialidade").value = especialidade || '';
    document.getElementById("consultorio").value = numeroConsultorio || '';
    document.getElementById("salario").value = salario || '';

    // Muda o botão para "Atualizar" e altera a cor
    const btnSalvar = document.getElementById("btn-salvar");
    btnSalvar.textContent = "Atualizar";
    btnSalvar.classList.add("btn-atualizar");

    // Mostra o botão "Cancelar"
    document.getElementById("btn-cancelar").style.display = "inline-block";
}

async function atualizarMedico(e) {
    e.preventDefault();

    const id = document.getElementById("medico-id").value;
    if (!id) return adicionarMedico(e); // Se não tiver ID, é um POST

    const medico = {
        nome: document.getElementById("nome").value,
        crm: document.getElementById("crm").value,
        especialidade: document.getElementById("especialidade").value,
        numeroConsultorio: document.getElementById("consultorio").value,
        salario: parseFloat(document.getElementById("salario").value) || 0
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
        resetForm();
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

function resetForm() {
    const btnSalvar = document.getElementById("btn-salvar");
    btnSalvar.textContent = "Salvar";
    btnSalvar.classList.remove("btn-atualizar");
    document.getElementById("btn-cancelar").style.display = "none";
    document.getElementById("medico-id").value = "";
    document.getElementById("form-medico").reset();
}

document.getElementById("form-medico").addEventListener("submit", atualizarMedico);
window.onload = carregarMedicos;