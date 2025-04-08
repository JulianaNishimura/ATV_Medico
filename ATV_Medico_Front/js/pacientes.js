const baseUrl = 'http://localhost:8080/api';

async function carregarPacientes() {
  try {
    const response = await fetch(`${baseUrl}/pacientes`);
    if (!response.ok) throw new Error('Erro ao buscar pacientes.');
    const pacientes = await response.json();

    const tbody = document.querySelector('#pacientes-table tbody');
    tbody.innerHTML = '';

    for (const paciente of pacientes) {
      let nomeMedico = 'Sem médico ainda';

      try {
        // 1. Buscar as consultas do paciente
        const resConsultas = await fetch(
          `${baseUrl}/consultas/paciente/${paciente.id}`
        );
        if (resConsultas.ok) {
          const consultas = await resConsultas.json();

          if (consultas.length > 0) {
            // 2. Ordenar por data mais recente
            consultas.sort(
              (a, b) => new Date(b.dataHora) - new Date(a.dataHora)
            );

            const medicoId = consultas[0].medico;

            // 3. Buscar o nome do médico via ID
            const resMedico = await fetch(`${baseUrl}/medicos/${medicoId}`);
            if (resMedico.ok) {
              const medico = await resMedico.json();
              nomeMedico = medico.nome || 'Desconhecido';
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
                    <button onclick="editarPaciente(${paciente.id})">✏️</button>
                    <button onclick="deletarPaciente(${
                      paciente.id
                    })">🗑️</button>
                </td>
            `;
      tbody.appendChild(tr);
    }
  } catch (error) {
    console.error('Erro ao carregar pacientes:', error.message);
  }
}

// POST: Cadastrar novo paciente
document
  .getElementById('paciente-form')
  .addEventListener('submit', async (e) => {
    e.preventDefault();

    const id = document.getElementById('paciente-id').value;
    const nome = document.getElementById('paciente-nome').value;
    const cpf = document.getElementById('paciente-cpf').value;
    const dataNascimento = document.getElementById(
      'paciente-data-nascimento'
    ).value;
    const medicoId = document.getElementById('paciente-medico-id').value;

    const paciente = {
      nome,
      cpf,
      dataNascimento: dataNascimento,
      medicoId: parseInt(medicoId),
    };

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

      if (!response.ok) throw new Error('Erro ao salvar paciente.');
      document.getElementById('mensagem').textContent = id
        ? 'Paciente atualizado!'
        : 'Paciente cadastrado!';
      document.getElementById('paciente-form').reset();
      carregarPacientes();
    } catch (error) {
      document.getElementById('mensagem').textContent =
        'Erro: ' + error.message;
    }
  });

// PUT: Carregar dados para edição
async function editarPaciente(id) {
  try {
    const response = await fetch(`${baseUrl}/pacientes/${id}`);
    if (!response.ok) throw new Error('Erro ao buscar paciente.');
    const paciente = await response.json();

    document.getElementById('paciente-id').value = paciente.id;
    document.getElementById('paciente-nome').value = paciente.nome;
    document.getElementById('paciente-cpf').value = paciente.cpf;
    document.getElementById('paciente-data-nascimento').value =
      paciente.dataNascimento;
    document.getElementById('paciente-medico-id').value = paciente.medico.id;
  } catch (error) {
    console.error('Erro ao carregar paciente:', error.message);
  }
}

// DELETE: Remover paciente
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

// Ao carregar a página
window.onload = async () => {
  await carregarMapaMedicos();
  carregarPacientes();
};
