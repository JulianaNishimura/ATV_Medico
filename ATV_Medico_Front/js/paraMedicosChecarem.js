const baseUrl = 'http://localhost:8080/api/medicos/crm';

async function consultarDadosMedico(e) {
  e.preventDefault();
  const crm = document.getElementById('medico-crm').value.trim();
  const mensagem = document.getElementById('mensagem');

  try {
    const response = await fetch(`${baseUrl}/${crm}`);
    if (!response.ok)
      throw new Error('CRM não encontrado ou erro na consulta.');
    const medico = await response.json();

    mensagem.textContent = 'Dados carregados com sucesso!';
    mensagem.style.color = '#0066cc';

    carregarConsultas(medico.consultas || []);
    carregarPacientes(medico.pacientes || []);
  } catch (err) {
    mensagem.textContent = err.message;
    mensagem.style.color = 'red';
    document.getElementById('consultas-section').style.display = 'none';
    document.getElementById('pacientes-section').style.display = 'none';
  }
}

function carregarConsultas(consultas) {
  const tbody = document.querySelector('#consultas-table tbody');
  tbody.innerHTML = '';
  const section = document.getElementById('consultas-section');

  if (!consultas || consultas.length === 0) {
    tbody.innerHTML =
      "<tr><td colspan='5'>Nenhuma consulta encontrada.</td></tr>";
  } else {
    consultas.forEach((consulta) => {
      const tr = document.createElement('tr');
      tr.innerHTML = `
                <td>${new Date(consulta.dataHora).toLocaleString('pt-BR')}</td>
                <td>${consulta.pacienteNome || 'Desconhecido'}</td>
                <td>${consulta.consultorio || '-'}</td>
                <td>${consulta.valor ? consulta.valor.toFixed(2) : '0.00'}</td>
                <td>${
                  consulta.dataRetorno
                    ? new Date(consulta.dataRetorno).toLocaleDateString('pt-BR')
                    : 'Nenhum'
                }</td>
            `;
      tbody.appendChild(tr);
    });
  }
  section.style.display = 'block';
}

function carregarPacientes(pacientes) {
  const tbody = document.querySelector('#pacientes-table tbody');
  tbody.innerHTML = '';
  const section = document.getElementById('pacientes-section');

  if (!pacientes || pacientes.length === 0) {
    tbody.innerHTML =
      "<tr><td colspan='3'>Nenhum paciente encontrado.</td></tr>";
  } else {
    pacientes.forEach((paciente) => {
      const tr = document.createElement('tr');
      tr.innerHTML = `
                <td>${paciente.nome}</td>
                <td>${paciente.cpf}</td>
                <td>${calcularIdade(paciente.dataNascimento)}</td>
            `;
      tbody.appendChild(tr);
    });
  }
  section.style.display = 'block';
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

document
  .getElementById('medico-login-form')
  .addEventListener('submit', consultarDadosMedico);
