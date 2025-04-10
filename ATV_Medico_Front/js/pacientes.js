const baseUrl = 'http://localhost:8080/api';

async function carregarPacientes() {
  try {
    // Carrega todos os médicos primeiro
    const resMedicos = await fetch(`${baseUrl}/medicos`);
    const medicos = resMedicos.ok ? await resMedicos.json() : [];

    const response = await fetch(`${baseUrl}/pacientes`);
    if (!response.ok) throw new Error('Erro ao buscar pacientes.');
    const pacientes = await response.json();

    const tbody = document.querySelector('#pacientes-table tbody');
    tbody.innerHTML = '';

    for (const paciente of pacientes) {
      let nomeMedico = 'Sem médico ainda';

      try {
        const resConsultas = await fetch(`${baseUrl}/consultas/paciente/${paciente.id}`);
        if (resConsultas.ok) {
          const consultas = await resConsultas.json();

          if (consultas.length > 0) {
            consultas.sort((a, b) => new Date(b.dataHora) - new Date(a.dataHora));
            const medicoId = consultas[0].medicoId;
            const medico = medicos.find((m) => m.id === medicoId);
            if (medico) {
              nomeMedico = `${medico.nome} (ID: ${medico.id})`;
            }
          }
        }
      } catch (err) {
        console.warn(`Erro ao buscar médico do paciente ${paciente.id}:`, err);
      }

      const tr = document.createElement('tr');
      tr.innerHTML = `
        <td>${paciente.id}</td>
        <td>${paciente.nome}</td>
        <td>${paciente.cpf}</td>
        <td>${calcularIdade(paciente.dataNascimento)} anos</td>
        <td>${nomeMedico}</td>
        <td>
          <button onclick="editarPaciente(${paciente.id}, '${paciente.nome}', '${paciente.cpf}', '${paciente.dataNascimento}')">✏️Editar</button>
          <button onclick="deletarPaciente(${paciente.id})">🗑️Excluir</button>
        </td>
      `;
      tbody.appendChild(tr);
    }
  } catch (error) {
    console.error('Erro ao carregar pacientes:', error.message);
  }
}

document.getElementById('paciente-form').addEventListener('submit', async (e) => {
  e.preventDefault();

  const id = document.getElementById('paciente-id').value;
  const nome = document.getElementById('paciente-nome').value;
  const cpf = document.getElementById('paciente-cpf').value;
  const dataNascimento = document.getElementById('paciente-data-nascimento').value;

  const paciente = { nome, cpf, dataNascimento };

  try {
    let method = 'POST';
    let url = `${baseUrl}/pacientes`;

    if (id) {
      method = 'PUT';
      url += `/${id}`;
    }

    const response = await fetch(url, {
      method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(paciente),
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(errorText || 'Erro ao salvar paciente.');
    }
    document.getElementById('mensagem').textContent = id ? 'Paciente atualizado!' : 'Paciente cadastrado!';
    document.getElementById('paciente-form').reset();
    resetForm();
    carregarPacientes();
  } catch (error) {
    document.getElementById('mensagem').textContent = 'Erro: ' + error.message;
  }
});

function editarPaciente(id, nome, cpf, dataNascimento) {
  // Preenche os campos com os dados da tabela
  document.getElementById('paciente-id').value = id;
  document.getElementById('paciente-nome').value = nome;
  document.getElementById('paciente-cpf').value = cpf;
  document.getElementById('paciente-data-nascimento').value = dataNascimento;

  // Muda o botão para "Atualizar" e altera a cor
  const btnSalvar = document.getElementById('btn-salvar');
  btnSalvar.textContent = "Atualizar";
  btnSalvar.classList.add('btn-atualizar');

  // Mostra o botão "Cancelar"
  document.getElementById('btn-cancelar').style.display = 'inline-block';
}

async function deletarPaciente(id) {
  if (!confirm('Tem certeza que deseja deletar este paciente?')) return;

  try {
    const response = await fetch(`${baseUrl}/pacientes/${id}`, {
      method: 'DELETE',
    });
    if (!response.ok) throw new Error('Erro ao deletar paciente.');
    carregarPacientes();
  } catch (error) {
    console.error('Erro ao deletar paciente:', error.message);
  }
}

function calcularIdade(dataNascimento) {
  const hoje = new Date();
  const nascimento = new Date(dataNascimento);
  let idade = hoje.getFullYear() - nascimento.getFullYear();
  const mes = hoje.getMonth() - nascimento.getMonth();
  if (mes < 0 || (mes === 0 && hoje.getDate() < nascimento.getDate())) {
    idade--;
  }
  return idade;
}

function resetForm() {
  const btnSalvar = document.getElementById('btn-salvar');
  btnSalvar.textContent = "Salvar";
  btnSalvar.classList.remove('btn-atualizar');
  document.getElementById('btn-cancelar').style.display = 'none';
  document.getElementById('paciente-id').value = "";
  document.getElementById('paciente-form').reset();
  document.getElementById('mensagem').textContent = '';
}

window.onload = async () => {
  carregarPacientes();
};