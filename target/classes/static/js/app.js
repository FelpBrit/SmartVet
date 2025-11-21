// ========================================
// HEALTHPET - VERSÃO ULTRA CORRIGIDA
// Testado e 100% Funcional
// ========================================

const API_URL = 'http://localhost:8080/api/animais';
const API_PRONTUARIO = 'http://localhost:8080/api/prontuarios';
const API_VACINA = 'http://localhost:8080/api/vacinas';

let animaisCache = [];
let animalAtual = null;

// ========== INICIALIZAÇÃO ==========
document.addEventListener('DOMContentLoaded', function() {
    console.log('🐾 HealthPet iniciado!');
    
    criarModais();
    inicializarNavegacao();
    carregarDados();
    configurarDataAtual();
    
    console.log('✅ Inicialização completa!');
});

function carregarDados() {
    carregarAnimais();
    carregarEstatisticas();
}

// ========== NAVEGAÇÃO ==========
function inicializarNavegacao() {
    console.log('Configurando navegação...');
    
    // Todos os elementos com data-section
    const elementos = document.querySelectorAll('[data-section]');
    console.log('Elementos encontrados:', elementos.length);
    
    elementos.forEach(function(elemento) {
        elemento.addEventListener('click', function(e) {
            e.preventDefault();
            const secao = elemento.getAttribute('data-section');
            console.log('Clicou em:', secao);
            navegarPara(secao);
        });
    });
}

function navegarPara(secao) {
    console.log('Navegando para:', secao);
    
    // Esconder todas
    const todasSecoes = document.querySelectorAll('.content-section');
    todasSecoes.forEach(function(s) {
        s.classList.remove('active');
    });
    
    // Mostrar a selecionada
    const secaoAlvo = document.getElementById(secao);
    if (secaoAlvo) {
        secaoAlvo.classList.add('active');
        console.log('✅ Seção mostrada:', secao);
        
        // Atualizar navbar
        document.querySelectorAll('.nav-link').forEach(function(link) {
            link.classList.remove('active');
        });
        
        const linkAtivo = document.querySelector('.nav-link[data-section="' + secao + '"]');
        if (linkAtivo) {
            linkAtivo.classList.add('active');
        }
        
        // Carregar dados específicos
        if (secao === 'animais') carregarAnimais();
        if (secao === 'vacinas') carregarTodasVacinas();
    } else {
        console.error('❌ Seção não encontrada:', secao);
    }
}

// ========== FORMULÁRIO ==========
function alterarFormulario() {
    const tipo = document.getElementById('tipoAnimal').value;
    const camposCachorro = document.getElementById('camposCachorro');
    const camposGato = document.getElementById('camposGato');
    const especie = document.getElementById('especie');
    
    camposCachorro.style.display = 'none';
    camposGato.style.display = 'none';
    
    if (tipo === 'cachorro') {
        camposCachorro.style.display = 'block';
        especie.value = 'Canina';
    } else if (tipo === 'gato') {
        camposGato.style.display = 'block';
        especie.value = 'Felina';
    } else if (tipo === 'outro') {
        especie.value = '';
    }
}

function cadastrarAnimal(event) {
    event.preventDefault();
    console.log('Cadastrando animal...');
    
    const tipo = document.getElementById('tipoAnimal').value;
    
    if (!tipo) {
        mostrarAlerta('❌ Selecione o tipo de animal', 'warning');
        return;
    }
    
    let endpoint = API_URL;
    
    const dados = {
        nome: document.getElementById('nome').value,
        especie: document.getElementById('especie').value,
        idade: parseFloat(document.getElementById('idade').value),
        nomeDono: document.getElementById('nomeDono').value,
        telefone: document.getElementById('telefone').value,
        raca: document.getElementById('raca').value || 'Não informado'
    };
    
    if (tipo === 'cachorro') {
        endpoint += '/cachorro';
        dados.porte = document.getElementById('porte').value;
    } else if (tipo === 'gato') {
        endpoint += '/gato';
        dados.pelagem = document.getElementById('pelagem').value;
        dados.temperamento = document.getElementById('temperamento').value;
    }
    
    console.log('Dados:', dados);
    console.log('Endpoint:', endpoint);
    
    fetch(endpoint, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(dados)
    })
    .then(function(response) {
        if (response.ok) {
            return response.json();
        } else {
            throw new Error('Erro ao cadastrar');
        }
    })
    .then(function(data) {
        console.log('✅ Animal cadastrado:', data);
        mostrarAlerta('✅ Animal cadastrado com sucesso!', 'success');
        document.getElementById('formCadastro').reset();
        document.getElementById('tipoAnimal').value = '';
        carregarDados();
        setTimeout(function() { navegarPara('animais'); }, 1500);
    })
    .catch(function(error) {
        console.error('❌ Erro:', error);
        mostrarAlerta('❌ Erro ao cadastrar: ' + error.message, 'danger');
    });
}

// ========== LISTAGEM ==========
function carregarAnimais() {
    console.log('Carregando animais...');
    
    fetch(API_URL)
    .then(function(response) { return response.json(); })
    .then(function(animais) {
        console.log('✅ Animais carregados:', animais.length);
        animaisCache = animais;
        exibirAnimais(animais);
    })
    .catch(function(error) {
        console.error('❌ Erro ao carregar:', error);
        mostrarAlerta('❌ Erro ao carregar animais', 'danger');
    });
}

function exibirAnimais(animais) {
    const container = document.getElementById('listaAnimais');
    
    if (animais.length === 0) {
        container.innerHTML = '<div class="col-12"><div class="empty-state"><i class="bi bi-inbox"></i><h3>Nenhum animal cadastrado</h3><button class="btn btn-primary btn-lg mt-3" onclick="navegarPara(\'cadastrar\')"><i class="bi bi-plus-circle me-2"></i>Cadastrar Animal</button></div></div>';
        return;
    }
    
    let html = '';
    animais.forEach(function(animal) {
        html += criarCardAnimal(animal);
    });
    
    container.innerHTML = html;
}

function criarCardAnimal(animal) {
    const tipo = animal.porte ? 'cachorro' : (animal.pelagem ? 'gato' : 'animal');
    const emoji = tipo === 'cachorro' ? '🐕' : (tipo === 'gato' ? '🐈' : '🐾');
    const cor = tipo === 'cachorro' ? 'warning' : (tipo === 'gato' ? 'info' : 'success');
    
    return '<div class="col-md-6 col-lg-4"><div class="card animal-card shadow h-100">' +
        '<div class="animal-header bg-gradient-' + cor + '"><div class="d-flex justify-content-between align-items-start">' +
        '<h4 class="mb-0">' + emoji + ' ' + animal.nome + '</h4>' +
        '<span class="badge animal-badge">ID: ' + animal.id + '</span></div></div>' +
        '<div class="card-body">' +
        '<p class="mb-2"><i class="bi bi-tag me-2"></i><strong>Espécie:</strong> ' + animal.especie + '</p>' +
        '<p class="mb-2"><i class="bi bi-award me-2"></i><strong>Raça:</strong> ' + animal.raca + '</p>' +
        '<p class="mb-2"><i class="bi bi-calendar me-2"></i><strong>Idade:</strong> ' + animal.idadeFormatada + '</p>' +
        '<p class="mb-2"><i class="bi bi-person me-2"></i><strong>Dono:</strong> ' + animal.nomeDono + '</p>' +
        '<p class="mb-2"><i class="bi bi-telephone me-2"></i><strong>Tel:</strong> ' + animal.telefone + '</p>' +
        (animal.porte ? '<p class="mb-2"><i class="bi bi-rulers me-2"></i><strong>Porte:</strong> ' + animal.porte + '</p>' : '') +
        (animal.pelagem ? '<p class="mb-2"><i class="bi bi-brush me-2"></i><strong>Pelagem:</strong> ' + animal.pelagem + '</p>' : '') +
        '</div>' +
        '<div class="card-footer bg-transparent p-3"><div class="d-grid gap-2">' +
        '<button onclick="abrirModalEditar(' + animal.id + ')" class="btn btn-warning btn-sm"><i class="bi bi-pencil me-1"></i>Editar</button>' +
        '<div class="row g-2">' +
        '<div class="col-6"><button onclick="abrirModalProntuario(' + animal.id + ')" class="btn btn-info btn-sm w-100"><i class="bi bi-clipboard-heart me-1"></i>Prontuário</button></div>' +
        '<div class="col-6"><button onclick="abrirModalVacinasAnimal(' + animal.id + ')" class="btn btn-success btn-sm w-100"><i class="bi bi-shield-check me-1"></i>Vacinas</button></div>' +
        '</div>' +
        '<button onclick="removerAnimal(' + animal.id + ')" class="btn btn-danger btn-sm"><i class="bi bi-trash me-1"></i>Remover</button>' +
        '</div></div></div></div>';
}

function filtrarAnimais() {
    const filtro = document.getElementById('filtroNome').value.toLowerCase();
    const filtrados = animaisCache.filter(function(a) {
        return a.nome.toLowerCase().includes(filtro) || a.id.toString().includes(filtro);
    });
    exibirAnimais(filtrados);
}

// ========== BUSCA ==========
function buscarAnimal() {
    const nome = document.getElementById('buscaNome').value;
    const id = document.getElementById('buscaId').value;
    
    if (!nome && !id) {
        mostrarAlerta('❌ Digite um nome ou ID', 'warning');
        return;
    }
    
    let url = API_URL;
    if (id) {
        url += '/' + id;
    } else if (nome) {
        url += '/buscar?nome=' + nome;
    }
    
    fetch(url)
    .then(function(response) { return response.json(); })
    .then(function(resultado) {
        const animais = Array.isArray(resultado) ? resultado : [resultado];
        const container = document.getElementById('resultadoBusca');
        
        if (animais.length === 0) {
            container.innerHTML = '<div class="alert alert-info">Nenhum resultado encontrado</div>';
        } else {
            let html = '<div class="row g-4">';
            animais.forEach(function(a) {
                html += criarCardAnimal(a);
            });
            html += '</div>';
            container.innerHTML = html;
            mostrarAlerta('✅ ' + animais.length + ' encontrado(s)!', 'success');
        }
    })
    .catch(function(error) {
        console.error('Erro:', error);
        mostrarAlerta('❌ Erro ao buscar', 'danger');
    });
}

// ========== EDIÇÃO ==========
function abrirModalEditar(id) {
    fetch(API_URL + '/' + id)
    .then(function(response) { return response.json(); })
    .then(function(animal) {
        document.getElementById('editId').value = animal.id;
        document.getElementById('editNome').value = animal.nome;
        document.getElementById('editEspecie').value = animal.especie;
        document.getElementById('editIdade').value = animal.idade;
        document.getElementById('editRaca').value = animal.raca;
        document.getElementById('editNomeDono').value = animal.nomeDono;
        document.getElementById('editTelefone').value = animal.telefone;
        
        const camposCachorro = document.getElementById('editCamposCachorro');
        const camposGato = document.getElementById('editCamposGato');
        
        camposCachorro.style.display = 'none';
        camposGato.style.display = 'none';
        
        if (animal.porte) {
            camposCachorro.style.display = 'block';
            document.getElementById('editPorte').value = animal.porte;
        } else if (animal.pelagem) {
            camposGato.style.display = 'block';
            document.getElementById('editPelagem').value = animal.pelagem;
            document.getElementById('editTemperamento').value = animal.temperamento;
        }
        
        const modal = new bootstrap.Modal(document.getElementById('modalEditar'));
        modal.show();
    })
    .catch(function(error) {
        console.error('Erro:', error);
        mostrarAlerta('❌ Erro ao carregar dados', 'danger');
    });
}

function salvarEdicao(event) {
    event.preventDefault();
    
    const id = document.getElementById('editId').value;
    const dados = {
        nome: document.getElementById('editNome').value,
        especie: document.getElementById('editEspecie').value,
        idade: parseFloat(document.getElementById('editIdade').value),
        nomeDono: document.getElementById('editNomeDono').value,
        telefone: document.getElementById('editTelefone').value,
        raca: document.getElementById('editRaca').value
    };
    
    const camposCachorro = document.getElementById('editCamposCachorro');
    if (camposCachorro.style.display !== 'none') {
        dados.porte = document.getElementById('editPorte').value;
    }
    
    const camposGato = document.getElementById('editCamposGato');
    if (camposGato.style.display !== 'none') {
        dados.pelagem = document.getElementById('editPelagem').value;
        dados.temperamento = document.getElementById('editTemperamento').value;
    }
    
    fetch(API_URL + '/' + id, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(dados)
    })
    .then(function(response) {
        if (response.ok) {
            mostrarAlerta('✅ Animal atualizado!', 'success');
            bootstrap.Modal.getInstance(document.getElementById('modalEditar')).hide();
            carregarDados();
        } else {
            throw new Error('Erro ao atualizar');
        }
    })
    .catch(function(error) {
        console.error('Erro:', error);
        mostrarAlerta('❌ Erro ao atualizar', 'danger');
    });
}

// ========== REMOÇÃO ==========
function removerAnimal(id) {
    if (!confirm('Tem certeza que deseja remover este animal?')) return;
    
    fetch(API_URL + '/' + id, { method: 'DELETE' })
    .then(function(response) {
        if (response.ok) {
            mostrarAlerta('✅ Animal removido!', 'success');
            carregarDados();
        } else {
            throw new Error('Erro ao remover');
        }
    })
    .catch(function(error) {
        console.error('Erro:', error);
        mostrarAlerta('❌ Erro ao remover', 'danger');
    });
}

// ========== PRONTUÁRIO ==========
function abrirModalProntuario(id) {
    fetch(API_URL + '/' + id)
    .then(function(response) { return response.json(); })
    .then(function(animal) {
        animalAtual = animal;
        document.getElementById('prontuarioAnimalId').value = id;
        document.getElementById('prontuarioNomeAnimal').textContent = 'Animal: ' + animal.nome;
        
        return fetch(API_PRONTUARIO + '/animal/' + id);
    })
    .then(function(response) { return response.json(); })
    .then(function(prontuario) {
        document.getElementById('prontuarioPeso').value = prontuario.peso || '';
        document.getElementById('prontuarioAltura').value = prontuario.altura || '';
        document.getElementById('prontuarioAlergias').value = prontuario.alergias || '';
        document.getElementById('prontuarioMedicamentos').value = prontuario.medicamentosEmUso || '';
        document.getElementById('prontuarioCondicoes').value = prontuario.condicoesPreExistentes || '';
        document.getElementById('prontuarioObservacoes').value = prontuario.observacoes || '';
        
        const modal = new bootstrap.Modal(document.getElementById('modalProntuario'));
        modal.show();
    })
    .catch(function(error) {
        console.error('Erro:', error);
        mostrarAlerta('❌ Erro ao carregar prontuário', 'danger');
    });
}

function salvarProntuario(event) {
    event.preventDefault();
    
    const animalId = document.getElementById('prontuarioAnimalId').value;
    const dados = {
        peso: parseFloat(document.getElementById('prontuarioPeso').value) || null,
        altura: parseFloat(document.getElementById('prontuarioAltura').value) || null,
        alergias: document.getElementById('prontuarioAlergias').value,
        medicamentosEmUso: document.getElementById('prontuarioMedicamentos').value,
        condicoesPreExistentes: document.getElementById('prontuarioCondicoes').value,
        observacoes: document.getElementById('prontuarioObservacoes').value
    };
    
    fetch(API_PRONTUARIO + '/animal/' + animalId, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(dados)
    })
    .then(function(response) {
        if (response.ok) {
            mostrarAlerta('✅ Prontuário salvo!', 'success');
            bootstrap.Modal.getInstance(document.getElementById('modalProntuario')).hide();
        } else {
            throw new Error('Erro ao salvar');
        }
    })
    .catch(function(error) {
        console.error('Erro:', error);
        mostrarAlerta('❌ Erro ao salvar', 'danger');
    });
}

// ========== VACINAS ==========
function abrirModalVacinasAnimal(id) {
    console.log('🔹 Abrindo modal de vacinas para animal ID:', id);
    
    fetch(API_URL + '/' + id)
    .then(function(response) { return response.json(); })
    .then(function(animal) {
        animalAtual = animal;
        console.log('✅ Animal carregado:', animal);
        document.getElementById('vacinasNomeAnimal').textContent = 'Animal: ' + animal.nome;
        
        return fetch(API_VACINA + '/animal/' + id);
    })
    .then(function(response) { return response.json(); })
    .then(function(vacinas) {
        console.log('✅ Vacinas carregadas:', vacinas);
        const container = document.getElementById('listaVacinasAnimal');
        
        if (vacinas.length === 0) {
            container.innerHTML = '<div class="alert alert-info">Nenhuma vacina registrada</div>';
        } else {
            let html = '';
            vacinas.forEach(function(v) {
                html += criarCardVacina(v);
            });
            container.innerHTML = html;
        }
        
        const modal = new bootstrap.Modal(document.getElementById('modalVacinasAnimal'));
        modal.show();
        console.log('✅ Modal aberto');
    })
    .catch(function(error) {
        console.error('❌ Erro:', error);
        mostrarAlerta('❌ Erro ao carregar vacinas', 'danger');
    });
}

function criarCardVacina(v) {
    const statusBadge = v.completa ? 'emdia' : 'proxima';
    
    return '<div class="vacina-card">' +
        '<h5><i class="bi bi-shield-check me-2"></i>' + v.nome + ' ' +
        '<span class="badge badge-' + statusBadge + '">' + (v.completa ? 'Completa' : 'Pendente') + '</span></h5>' +
        '<p><strong>Aplicada:</strong> ' + formatarData(v.dataAplicacao) + '</p>' +
        (v.proximaDose ? '<p><strong>Próxima:</strong> ' + formatarData(v.proximaDose) + '</p>' : '') +
        (v.lote ? '<p><strong>Lote:</strong> ' + v.lote + '</p>' : '') +
        '<div class="mt-2">' +
        (!v.completa ? '<button onclick="marcarVacinaCompleta(' + v.id + ')" class="btn btn-success btn-sm">✓ Completa</button> ' : '') +
        '<button onclick="deletarVacina(' + v.id + ')" class="btn btn-danger btn-sm">🗑️ Remover</button>' +
        '</div></div>';
}

function abrirModalNovaVacina() {
    console.log('🔹 Abrindo modal de nova vacina');
    console.log('🔹 Animal atual:', animalAtual);
    
    if (!animalAtual || !animalAtual.id) {
        console.error('❌ Animal não identificado!');
        mostrarAlerta('❌ Erro: Animal não identificado', 'danger');
        return;
    }
    
    document.getElementById('vacinaAnimalId').value = animalAtual.id;
    document.getElementById('formNovaVacina').reset();
    configurarDataAtual();
    
    console.log('🔹 ID do animal configurado:', animalAtual.id);
    
    // Fechar modal anterior
    const modalAnterior = bootstrap.Modal.getInstance(document.getElementById('modalVacinasAnimal'));
    if (modalAnterior) {
        modalAnterior.hide();
    }
    
    setTimeout(function() {
        const modal = new bootstrap.Modal(document.getElementById('modalNovaVacina'));
        modal.show();
        console.log('✅ Modal de nova vacina aberto');
    }, 300);
}

function registrarVacina(event) {
    event.preventDefault();
    console.log('🔹 Registrando vacina...');
    
    const animalId = document.getElementById('vacinaAnimalId').value;
    console.log('🔹 ID do animal:', animalId);
    
    if (!animalId) {
        console.error('❌ ID do animal não encontrado!');
        mostrarAlerta('❌ Erro: ID do animal não encontrado', 'danger');
        return;
    }
    
    const dados = {
        nome: document.getElementById('vacinaNome').value,
        dataAplicacao: document.getElementById('vacinaData').value,
        proximaDose: document.getElementById('vacinaProximaDose').value || null,
        lote: document.getElementById('vacinaLote').value || null,
        veterinario: document.getElementById('vacinaVeterinario').value || null,
        observacoes: document.getElementById('vacinaObservacoes').value || null,
        completa: document.getElementById('vacinaCompleta').checked
    };
    
    console.log('🔹 Dados da vacina:', dados);
    console.log('🔹 URL:', API_VACINA + '/animal/' + animalId);
    
    fetch(API_VACINA + '/animal/' + animalId, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(dados)
    })
    .then(function(response) {
        console.log('🔹 Resposta recebida:', response.status);
        if (response.ok) {
            return response.json();
        } else {
            return response.text().then(function(text) {
                console.error('❌ Erro do servidor:', text);
                throw new Error('Erro ao registrar: ' + text);
            });
        }
    })
    .then(function(resultado) {
        console.log('✅ Vacina registrada:', resultado);
        mostrarAlerta('✅ Vacina registrada com sucesso!', 'success');
        bootstrap.Modal.getInstance(document.getElementById('modalNovaVacina')).hide();
        
        setTimeout(function() {
            abrirModalVacinasAnimal(animalId);
        }, 500);
        
        carregarEstatisticas();
    })
    .catch(function(error) {
        console.error('❌ Erro completo:', error);
        mostrarAlerta('❌ Erro: ' + error.message, 'danger');
    });
}

function marcarVacinaCompleta(id) {
    fetch(API_VACINA + '/' + id + '/completa', { method: 'PUT' })
    .then(function(response) {
        if (response.ok) {
            mostrarAlerta('✅ Vacina completa!', 'success');
            abrirModalVacinasAnimal(animalAtual.id);
            carregarEstatisticas();
        } else {
            throw new Error('Erro');
        }
    })
    .catch(function(error) {
        console.error('Erro:', error);
        mostrarAlerta('❌ Erro', 'danger');
    });
}

function deletarVacina(id) {
    if (!confirm('Remover esta vacina?')) return;
    
    fetch(API_VACINA + '/' + id, { method: 'DELETE' })
    .then(function(response) {
        if (response.ok) {
            mostrarAlerta('✅ Vacina removida!', 'success');
            abrirModalVacinasAnimal(animalAtual.id);
            carregarEstatisticas();
        } else {
            throw new Error('Erro');
        }
    })
    .catch(function(error) {
        console.error('Erro:', error);
        mostrarAlerta('❌ Erro', 'danger');
    });
}

function carregarTodasVacinas() {
    Promise.all([
        fetch(API_VACINA + '/vencidas').then(function(r) { return r.json(); }),
        fetch(API_VACINA + '/proximas').then(function(r) { return r.json(); })
    ])
    .then(function(resultados) {
        const vencidas = resultados[0];
        const proximas = resultados[1];
        
        const container = document.getElementById('listaTodasVacinas');
        const alertBox = document.getElementById('alertasVacinas');
        
        if (vencidas.length > 0 || proximas.length > 0) {
            alertBox.style.display = 'block';
            document.getElementById('mensagemAlertas').innerHTML = 
                '<strong>' + vencidas.length + '</strong> vencida(s) e <strong>' + proximas.length + '</strong> próxima(s)';
        } else {
            alertBox.style.display = 'none';
        }
        
        let html = '';
        
        if (vencidas.length > 0) {
            html += '<h4 class="text-danger">⚠️ Vencidas</h4>';
            vencidas.forEach(function(v) {
                html += criarCardVacina(v);
            });
        }
        
        if (proximas.length > 0) {
            html += '<h4 class="text-warning mt-4">⏰ Próximas</h4>';
            proximas.forEach(function(v) {
                html += criarCardVacina(v);
            });
        }
        
        if (!html) {
            html = '<div class="alert alert-success">✅ Todas em dia!</div>';
        }
        
        container.innerHTML = html;
    })
    .catch(function(error) {
        console.error('Erro:', error);
        mostrarAlerta('❌ Erro ao carregar', 'danger');
    });
}

// ========== ESTATÍSTICAS ==========
function carregarEstatisticas() {
    console.log('Carregando estatísticas...');
    
    // Carregar animais diretamente para contar
    fetch(API_URL)
    .then(function(response) { return response.json(); })
    .then(function(animais) {
        console.log('Todos os animais:', animais);
        
        let total = animais.length;
        let cachorros = 0;
        let gatos = 0;
        let outros = 0;
        
        animais.forEach(function(animal) {
            if (animal.porte) {
                cachorros++;
            } else if (animal.pelagem) {
                gatos++;
            } else {
                outros++;
            }
        });
        
        console.log('Contagem: Total=' + total + ', Cachorros=' + cachorros + ', Gatos=' + gatos + ', Outros=' + outros);
        
        document.getElementById('totalAnimais').textContent = total;
        document.getElementById('totalCachorros').textContent = cachorros;
        document.getElementById('totalGatos').textContent = gatos;
        document.getElementById('totalOutros').textContent = outros;
        
        // Carregar vacinas vencidas
        return fetch(API_VACINA + '/vencidas');
    })
    .then(function(response) { return response.json(); })
    .then(function(vencidas) {
        document.getElementById('vacinasVencidas').textContent = vencidas.length;
        console.log('✅ Estatísticas atualizadas');
    })
    .catch(function(error) {
        console.error('❌ Erro ao carregar estatísticas:', error);
    });
}

// ========== UTILIDADES ==========
function formatarData(data) {
    if (!data) return '';
    const d = new Date(data + 'T00:00:00');
    return d.toLocaleDateString('pt-BR');
}

function configurarDataAtual() {
    const hoje = new Date().toISOString().split('T')[0];
    const campoData = document.getElementById('vacinaData');
    if (campoData) {
        campoData.value = hoje;
        console.log('✅ Data configurada:', hoje);
    }
}

function mostrarAlerta(mensagem, tipo) {
    const container = document.getElementById('alertContainer');
    container.innerHTML = '<div class="alert alert-' + tipo + ' alert-dismissible fade show" role="alert">' +
        mensagem +
        '<button type="button" class="btn-close" data-bs-dismiss="alert"></button></div>';
    
    setTimeout(function() {
        const alert = container.querySelector('.alert');
        if (alert) {
            const bsAlert = bootstrap.Alert.getInstance(alert);
            if (bsAlert) bsAlert.close();
        }
    }, 5000);
}

// ========== MODAIS ==========
function criarModais() {
    const modalsHTML = 
        '<div class="modal fade" id="modalEditar" tabindex="-1">' +
        '<div class="modal-dialog modal-lg"><div class="modal-content">' +
        '<div class="modal-header bg-gradient-primary text-white">' +
        '<h5 class="modal-title"><i class="bi bi-pencil me-2"></i>Editar</h5>' +
        '<button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button></div>' +
        '<div class="modal-body"><form id="formEditar" onsubmit="salvarEdicao(event)">' +
        '<input type="hidden" id="editId"><div class="row g-3">' +
        '<div class="col-md-6"><label class="form-label">Nome *</label><input type="text" class="form-control" id="editNome" required></div>' +
        '<div class="col-md-6"><label class="form-label">Espécie *</label><input type="text" class="form-control" id="editEspecie" required></div>' +
        '<div class="col-md-6"><label class="form-label">Idade *</label><input type="number" class="form-control" id="editIdade" step="0.1" required></div>' +
        '<div class="col-md-6"><label class="form-label">Raça</label><input type="text" class="form-control" id="editRaca"></div>' +
        '<div class="col-md-6"><label class="form-label">Dono *</label><input type="text" class="form-control" id="editNomeDono" required></div>' +
        '<div class="col-md-6"><label class="form-label">Telefone *</label><input type="tel" class="form-control" id="editTelefone" required></div></div>' +
        '<div id="editCamposCachorro" style="display:none;" class="mt-3">' +
        '<label class="form-label">Porte</label><select class="form-select" id="editPorte">' +
        '<option value="Pequeno">Pequeno</option><option value="Médio">Médio</option><option value="Grande">Grande</option></select></div>' +
        '<div id="editCamposGato" style="display:none;" class="mt-3"><div class="row g-3">' +
        '<div class="col-6"><label class="form-label">Pelagem</label><select class="form-select" id="editPelagem">' +
        '<option value="Curta">Curta</option><option value="Média">Média</option><option value="Longa">Longa</option></select></div>' +
        '<div class="col-6"><label class="form-label">Temperamento</label><select class="form-select" id="editTemperamento">' +
        '<option value="Calmo">Calmo</option><option value="Ativo">Ativo</option><option value="Agressivo">Agressivo</option><option value="Tímido">Tímido</option></select></div></div></div>' +
        '<button type="submit" class="btn btn-success w-100 mt-3"><i class="bi bi-check me-2"></i>Salvar</button>' +
        '</form></div></div></div></div>' +
        
        '<div class="modal fade" id="modalProntuario" tabindex="-1">' +
        '<div class="modal-dialog modal-lg"><div class="modal-content">' +
        '<div class="modal-header bg-gradient-info text-white">' +
        '<h5 class="modal-title"><i class="bi bi-clipboard-heart me-2"></i>Prontuário</h5>' +
        '<button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button></div>' +
        '<div class="modal-body"><div class="info-box"><strong id="prontuarioNomeAnimal"></strong></div>' +
        '<form id="formProntuario" onsubmit="salvarProntuario(event)"><input type="hidden" id="prontuarioAnimalId">' +
        '<h6 class="fw-bold mb-3">📏 Medidas</h6><div class="row g-3 mb-4">' +
        '<div class="col-6"><label class="form-label">Peso (kg)</label><input type="number" class="form-control" id="prontuarioPeso" step="0.1"></div>' +
        '<div class="col-6"><label class="form-label">Altura (cm)</label><input type="number" class="form-control" id="prontuarioAltura" step="0.1"></div></div>' +
        '<h6 class="fw-bold mb-3">🩺 Informações Médicas</h6>' +
        '<div class="mb-3"><label class="form-label">Alergias</label><textarea class="form-control" id="prontuarioAlergias" rows="2"></textarea></div>' +
        '<div class="mb-3"><label class="form-label">Medicamentos</label><textarea class="form-control" id="prontuarioMedicamentos" rows="2"></textarea></div>' +
        '<div class="mb-3"><label class="form-label">Condições</label><textarea class="form-control" id="prontuarioCondicoes" rows="2"></textarea></div>' +
        '<div class="mb-3"><label class="form-label">Observações</label><textarea class="form-control" id="prontuarioObservacoes" rows="3"></textarea></div>' +
        '<button type="submit" class="btn btn-success w-100"><i class="bi bi-save me-2"></i>Salvar</button></form></div></div></div></div>' +
        
        '<div class="modal fade" id="modalVacinasAnimal" tabindex="-1">' +
        '<div class="modal-dialog modal-lg"><div class="modal-content">' +
        '<div class="modal-header bg-gradient-success text-white">' +
        '<h5 class="modal-title"><i class="bi bi-shield-check me-2"></i>Vacinas</h5>' +
        '<button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button></div>' +
        '<div class="modal-body"><div class="info-box"><strong id="vacinasNomeAnimal"></strong></div>' +
        '<button onclick="abrirModalNovaVacina()" class="btn btn-primary w-100 mb-3"><i class="bi bi-plus-circle me-2"></i>Adicionar Vacina</button>' +
        '<div id="listaVacinasAnimal"></div></div></div></div></div>' +
        
        '<div class="modal fade" id="modalNovaVacina" tabindex="-1">' +
        '<div class="modal-dialog"><div class="modal-content">' +
        '<div class="modal-header bg-gradient-success text-white">' +
        '<h5 class="modal-title"><i class="bi bi-shield-plus me-2"></i>Registrar Vacina</h5>' +
        '<button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button></div>' +
        '<div class="modal-body"><form id="formNovaVacina" onsubmit="registrarVacina(event)">' +
        '<input type="hidden" id="vacinaAnimalId">' +
        '<div class="mb-3"><label class="form-label">Nome da Vacina *</label><input type="text" class="form-control" id="vacinaNome" required></div>' +
        '<div class="row g-3 mb-3">' +
        '<div class="col-6"><label class="form-label">Data Aplicação *</label><input type="date" class="form-control" id="vacinaData" required></div>' +
        '<div class="col-6"><label class="form-label">Próxima Dose</label><input type="date" class="form-control" id="vacinaProximaDose"></div></div>' +
        '<div class="row g-3 mb-3">' +
        '<div class="col-6"><label class="form-label">Lote</label><input type="text" class="form-control" id="vacinaLote"></div>' +
        '<div class="col-6"><label class="form-label">Veterinário</label><input type="text" class="form-control" id="vacinaVeterinario"></div></div>' +
        '<div class="mb-3"><label class="form-label">Observações</label><textarea class="form-control" id="vacinaObservacoes" rows="2"></textarea></div>' +
        '<div class="form-check mb-3"><input class="form-check-input" type="checkbox" id="vacinaCompleta">' +
        '<label class="form-check-label" for="vacinaCompleta">Vacinação completa</label></div>' +
        '<button type="submit" class="btn btn-success w-100"><i class="bi bi-check-circle me-2"></i>Registrar</button>' +
        '</form></div></div></div></div>';
    
    document.getElementById('modalsContainer').innerHTML = modalsHTML;
    console.log('✅ Modais criados');
}

console.log('🐾 HealthPet JavaScript carregado!');