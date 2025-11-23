================================================================================
            SMARTVET – SISTEMA DE GERENCIAMENTO DE CLÍNICA VETERINÁRIA
================================================================================

Projeto Integrador – Sistemas de Informação  
Desenvolvido em Java Spring Boot com frontend web moderno  
Banco de dados: H2 Database  
Versão: 2.0  
Autor: Felipe Brito dos Santos Rocha  
Ano: 2025

================================================================================
                              DESCRIÇÃO DO PROJETO
================================================================================

O **SmartVet** é um sistema web desenvolvido para demonstrar, de forma clara e 
profissional, o funcionamento de um sistema de gerenciamento de clínica 
veterinária. Ele reúne organização estrutural de backend em **Spring Boot**, banco 
de dados **H2**, interface moderna com **Bootstrap**, além de funcionalidades como 
cadastro de animais, prontuários, vacinas e um completo modo escuro.

Nesta nova versão, o sistema foi expandido com:

- **Landing Page**
- **Tela de Login**
- **Sistema de autenticação via JavaScript (LocalStorage)**
- **Proteção de páginas internas**

Essas adições simulam o fluxo real de um sistema comercial, mantendo a simplicidade 
necessária para ambientes acadêmicos e protótipos.

O SmartVet segue o padrão **MVC**, com separação organizada entre:  
Controller • Service • Repository • Model • Views

================================================================================
                       ARQUITETURA E ESTRUTURA DO SISTEMA
================================================================================

📁 **Backend – Spring Boot (Java 17)**  
- Controllers (Animal, Vacina, Prontuário)  
- Services  
- Repositories (Spring Data JPA)  
- Models (Entidades)  
- Enums  
- Exception handlers  
- Aplicação principal  

📁 **Frontend – Páginas e Recursos**  
- **landing.html** (Novo)  
- **login.html** (Novo)  
- **auth.js** (Novo — autenticação)  
- index.html e páginas internas  
- CSS customizado (incluindo Dark Mode)  
- Bootstrap 5  
- JavaScript geral (app.js)

📁 **Banco de Dados**
- H2 Database (memória)
- Console acessível via navegador

================================================================================
                           FUNCIONALIDADES PRINCIPAIS
================================================================================

### 🐾 1. Cadastro de Animais  
O sistema permite:

- Inserir animais com nome, idade, espécie, pelagem, temperamento, raça e outros dados  
- Editar e excluir registros  
- Visualizar lista de todos os animais cadastrados  

Dados persistidos automaticamente no H2 via JPA.

---

### 📁 2. Prontuários Veterinários  
O prontuário permite:

- Registrar consultas e observações  
- Associar diretamente ao animal  
- Acompanhar histórico clínico básico  

---

### 💉 3. Controle de Vacinas  
Cadastro e gerenciamento de vacinas aplicadas, contendo:

- Tipo de vacina  
- Datas  
- Associações com o animal correspondente  

---

### 🌙 4. Dark Mode  
Tema escuro completo com:

- Variáveis CSS  
- Alternância dinâmica  
- Interface moderna e confortável  

---

### 📱 5. Responsividade  
Utilizando Bootstrap 5 para:

- Grid responsivo  
- Layout adaptado para mobile, tablet e desktop  
- Componentes modernos como cards, botões, containers e modais  

---

## ⭐ **NOVAS FUNCIONALIDADES ADICIONADAS**

### 🎨 6. Landing Page (Novo)
Página inicial com:

- Identidade visual  
- Botão de login  
- Layout leve e convidativo  

---

### 🔐 7. Sistema de Login (Novo – 100% Front-End)
Implementado para demonstração utilizando:

- **JavaScript + LocalStorage**  
- Sem backend ou tabela de usuários (fluxo simplificado)  

Permite:

- Acesso apenas a usuários autenticados  
- Redirecionamento automático  
- Logout com limpeza de sessão  

---

### 🧭 8. Proteção das Páginas Internas (Novo)
Cada página interna verifica:

if (!localStorage.getItem("user")) {
window.location.href = "login.html";
}

Impedindo acesso não autorizado.

---

### 📌 9. auth.js (Novo)
Arquivo responsável por:

- Validar usuário e senha fixos (ex.: admin/admin)  
- Armazenar o usuário no LocalStorage  
- Redirecionar após login  
- Aplicar logout  
- Bloquear páginas internas quando não autenticado  

---

================================================================================
                           TECNOLOGIAS UTILIZADAS
================================================================================

 🔧 **Backend**
- Java 17  
- Spring Boot 3.x  
- Spring Data JPA  
- Hibernate  
- Maven  

🎨 **Frontend**
- HTML5  
- CSS3  
- Bootstrap 5  
- JavaScript  
- Dark Mode personalizado  
- Autenticação via LocalStorage  

🗄 **Banco**
- H2 Database  
- Console SQL integrado

Ferramentas adicionais:  
- VS Code  
- Git e GitHub  

================================================================================
                   FUNCIONALIDADES DETALHADAS (POR MÓDULO)
================================================================================

### 🐶 Cadastro de Animais  
- CRUD completo  
- Persistência via JPA  
- Listagem responsiva  

### 📝 Prontuários  
- Registro de consultas  
- Observações gerais  
- Associação direta ao animal  

### 💉 Vacinas  
- Cadastro de vacinas  
- Datas e tipos  
- Gerenciamento independente  

### 🌙 Dark Mode  
- Tema claro/escuro  
- Alternância instantânea  
- CSS modular  

### 🔐 Sistema de Login (Novo)  
Fluxo simulado, ideal para protótipos:

Usuário → login.html
→ Validação (JS/localStorage)
→ index.html (se logado)
→ Logout → limpeza da sessão

### 🧭 Proteção de Rotas (Novo)
Bloqueio automático para evitar acesso sem login.

================================================================================
                         INSTRUÇÕES DE INSTALAÇÃO
================================================================================

1. Clonar o repositório  
git clone https://github.com/SEU_USUARIO/SEU_REPOSITORIO.git

2. Abrir no VS Code

3. Rodar backend:
mvn spring-boot:run

4. Acessar a Landing:
http://localhost:8080/landing.html

5. Login → redirecionado para área interna

6. Console do H2:
http://localhost:8080/h2-console

================================================================================
                               TROUBLESHOOTING
================================================================================

⚠ Página interna abre sem login  
→ Verifique o script de verificação de sessão

⚠ Login não funciona  
→ Confira se auth.js está sendo carregado  
→ Verifique o LocalStorage do navegador  

⚠ Bootstrap não carrega  
→ Confirme os caminhos na pasta /static  

⚠ Erro no H2  
→ Verifique a URL em application.properties  

================================================================================
                                  LICENÇA
================================================================================

Licença **MIT**. Livre para uso e modificações, com crédito ao autor.

================================================================================
                                   AUTOR
================================================================================

**Felipe Brito dos Santos Rocha**  
Desenvolvedor do SmartVet  

================================================================================
                               AGRADECIMENTOS
================================================================================

- Professores envolvidos  
- Colegas de curso  
- Comunidade de desenvolvimento Java e web  
- Projeto criado para fins acadêmicos  

================================================================================
                                     FIM
================================================================================