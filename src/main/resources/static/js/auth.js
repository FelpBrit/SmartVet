// ========================================
// SMARTVET - SISTEMA DE AUTENTICAÇÃO
// Versão 1.0
// ========================================

// Usuários demo do sistema
const USUARIOS_DEMO = [
    {
        id: 1,
        nome: 'Dr. João Silva',
        email: 'admin@smartvet.com',
        senha: 'admin123',
        cargo: 'Administrador',
        avatar: '👨‍⚕️',
        permissoes: ['admin', 'editar', 'deletar']
    },
    {
        id: 2,
        nome: 'Dra. Maria Santos',
        email: 'vet@smartvet.com',
        senha: 'vet123',
        cargo: 'Veterinária',
        avatar: '👩‍⚕️',
        permissoes: ['editar']
    }
];

// ========== FUNÇÕES DE AUTENTICAÇÃO ==========

/**
 * Realizar login do usuário
 */
function fazerLogin(event) {
    event.preventDefault();
    console.log('🔐 Tentando fazer login...');
    
    const email = document.getElementById('loginEmail').value.trim();
    const senha = document.getElementById('loginSenha').value;
    const lembrarMe = document.getElementById('lembrarMe').checked;
    
    // Validar campos vazios
    if (!email || !senha) {
        mostrarAlertaLogin('Por favor, preencha todos os campos', 'warning');
        return;
    }
    
    // Validar credenciais
    const usuario = validarCredenciais(email, senha);
    
    if (usuario) {
        // Login bem-sucedido
        console.log('✅ Login bem-sucedido:', usuario.nome);
        
        // Criar sessão
        criarSessao(usuario, lembrarMe);
        
        // Mostrar sucesso
        mostrarAlertaLogin('Login realizado com sucesso! Redirecionando...', 'success');
        
        // Redirecionar para o dashboard
        setTimeout(function() {
            window.location.href = 'index.html';
        }, 1000);
        
    } else {
        // Credenciais inválidas
        console.log('❌ Credenciais inválidas');
        mostrarAlertaLogin('Email ou senha incorretos. Tente novamente.', 'danger');
        
        // Limpar campo de senha
        document.getElementById('loginSenha').value = '';
        document.getElementById('loginSenha').focus();
    }
}

/**
 * Validar credenciais do usuário
 */
function validarCredenciais(email, senha) {
    return USUARIOS_DEMO.find(function(user) {
        return user.email === email && user.senha === senha;
    });
}

/**
 * Criar sessão do usuário
 */
function criarSessao(usuario, lembrarMe) {
    const sessao = {
        logado: true,
        usuario: {
            id: usuario.id,
            nome: usuario.nome,
            email: usuario.email,
            cargo: usuario.cargo,
            avatar: usuario.avatar,
            permissoes: usuario.permissoes
        },
        timestamp: Date.now(),
        lembrarMe: lembrarMe
    };
    
    // Salvar no localStorage
    localStorage.setItem('smartvet_session', JSON.stringify(sessao));
    console.log('💾 Sessão criada e salva');
}

/**
 * Verificar se usuário já está logado
 */
function verificarSeJaEstaLogado() {
    const sessao = obterSessao();
    
    if (sessao && sessao.logado) {
        console.log('✅ Usuário já está logado');
        
        // Se estiver na página de login, redirecionar
        if (window.location.pathname.includes('login.html')) {
            window.location.href = 'index.html';
        }
    }
}

/**
 * Obter sessão atual
 */
function obterSessao() {
    try {
        const sessaoString = localStorage.getItem('smartvet_session');
        if (sessaoString) {
            return JSON.parse(sessaoString);
        }
    } catch (error) {
        console.error('Erro ao obter sessão:', error);
    }
    return null;
}

/**
 * Verificar se está autenticado (proteção de rotas)
 */
function verificarAutenticacao() {
    const sessao = obterSessao();
    
    // Se não estiver na landing ou login, verificar autenticação
    const paginaPublica = window.location.pathname.includes('landing.html') || 
                          window.location.pathname.includes('login.html');
    
    if (!paginaPublica && (!sessao || !sessao.logado)) {
        console.log('❌ Não autenticado, redirecionando para login');
        window.location.href = 'login.html';
        return false;
    }
    
    return true;
}

/**
 * Fazer logout do sistema
 */
function fazerLogout() {
    console.log('👋 Fazendo logout...');
    
    // Confirmar logout
    if (confirm('Deseja realmente sair do sistema?')) {
        // Remover sessão
        localStorage.removeItem('smartvet_session');
        console.log('✅ Sessão removida');
        
        // Redirecionar para login
        window.location.href = 'login.html';
    }
}

/**
 * Obter usuário logado
 */
function obterUsuarioLogado() {
    const sessao = obterSessao();
    return sessao ? sessao.usuario : null;
}

/**
 * Mostrar informações do usuário logado
 */
function mostrarUsuarioLogado() {
    const usuario = obterUsuarioLogado();
    
    if (usuario) {
        console.log('👤 Usuário logado:', usuario.nome);
        
        // Procurar por elementos para mostrar info do usuário
        const userInfo = document.getElementById('userInfo');
        const userName = document.getElementById('userName');
        const userAvatar = document.getElementById('userAvatar');
        
        if (userName) {
            userName.textContent = usuario.nome;
        }
        
        if (userAvatar) {
            userAvatar.textContent = usuario.avatar;
        }
        
        if (userInfo) {
            userInfo.style.display = 'block';
        }
        
        // Adicionar botão de logout se não existir
        adicionarBotaoLogout();
    }
}

/**
 * Adicionar botão de logout na navbar
 */
function adicionarBotaoLogout() {
    const navbar = document.querySelector('.navbar-nav');
    
    if (navbar && !document.getElementById('btnLogout')) {
        const usuario = obterUsuarioLogado();
        
        const logoutItem = document.createElement('li');
        logoutItem.className = 'nav-item dropdown';
        logoutItem.innerHTML = 
            '<a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">' +
            '<span class="me-2">' + usuario.avatar + '</span>' + usuario.nome +
            '</a>' +
            '<ul class="dropdown-menu dropdown-menu-end glass-effect">' +
            '<li><span class="dropdown-item-text"><small>👤 ' + usuario.cargo + '</small></span></li>' +
            '<li><hr class="dropdown-divider"></li>' +
            '<li><a class="dropdown-item" href="#" onclick="fazerLogout(); return false;"><i class="bi bi-box-arrow-right me-2"></i>Sair</a></li>' +
            '</ul>';
        
        navbar.appendChild(logoutItem);
    }
}

/**
 * Mostrar alerta na tela de login
 */
function mostrarAlertaLogin(mensagem, tipo) {
    // Remover alerta anterior se existir
    const alertaAnterior = document.querySelector('.login-alert');
    if (alertaAnterior) {
        alertaAnterior.remove();
    }
    
    // Criar novo alerta
    const alerta = document.createElement('div');
    alerta.className = 'alert alert-' + tipo + ' alert-dismissible fade show login-alert';
    alerta.style.marginBottom = '1.5rem';
    alerta.style.borderRadius = '15px';
    alerta.style.fontWeight = '600';
    
    // Melhorar contraste no dark mode
    if (document.body.classList.contains('dark-mode')) {
        if (tipo === 'danger') {
            alerta.style.background = 'rgba(220, 53, 69, 0.15)';
            alerta.style.color = '#ff6b6b';
            alerta.style.border = '2px solid rgba(220, 53, 69, 0.3)';
        } else if (tipo === 'success') {
            alerta.style.background = 'rgba(40, 167, 69, 0.15)';
            alerta.style.color = '#51cf66';
            alerta.style.border = '2px solid rgba(40, 167, 69, 0.3)';
        } else if (tipo === 'warning') {
            alerta.style.background = 'rgba(255, 193, 7, 0.15)';
            alerta.style.color = '#ffd43b';
            alerta.style.border = '2px solid rgba(255, 193, 7, 0.3)';
        }
    }
    
    alerta.innerHTML = mensagem + 
        '<button type="button" class="btn-close" data-bs-dismiss="alert" style="filter: brightness(1.5);"></button>';
    
    // Inserir antes do formulário
    const form = document.getElementById('loginForm');
    form.parentNode.insertBefore(alerta, form);
    
    // Animar entrada
    alerta.style.animation = 'slideDown 0.5s ease';
    
    // Auto-remover após 5 segundos
    setTimeout(function() {
        if (alerta && alerta.parentNode) {
            alerta.style.animation = 'slideUp 0.5s ease';
            setTimeout(function() {
                if (alerta && alerta.parentNode) {
                    alerta.remove();
                }
            }, 500);
        }
    }, 5000);
}

/**
 * Verificar permissão do usuário
 */
function temPermissao(permissao) {
    const usuario = obterUsuarioLogado();
    return usuario && usuario.permissoes && usuario.permissoes.includes(permissao);
}

/**
 * Verificar se é admin
 */
function isAdmin() {
    return temPermissao('admin');
}

// ========== INICIALIZAÇÃO ==========

// Quando o documento carregar
document.addEventListener('DOMContentLoaded', function() {
    console.log('🔐 Sistema de autenticação carregado');
    
    // Se não estiver em página pública, verificar autenticação
    const paginaPublica = window.location.pathname.includes('landing.html') || 
                          window.location.pathname.includes('login.html') ||
                          window.location.pathname === '/' ||
                          window.location.pathname === '';
    
    if (!paginaPublica) {
        // Verificar se está autenticado
        if (verificarAutenticacao()) {
            // Mostrar informações do usuário
            mostrarUsuarioLogado();
        }
    }
});

console.log('🔐 auth.js carregado com sucesso!');