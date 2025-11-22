================================================================================
            SMARTVET – SISTEMA DE GERENCIAMENTO DE CLÍNICA VETERINÁRIA
================================================================================

Projeto Integrador – Sistemas de Informação  
Desenvolvido em Java Spring Boot com frontend web moderno  
Banco de dados: H2 Database  
Versão: 1.0  
Autor: Felipe Brito dos Santos Rocha  
Ano: 2025

================================================================================
                              DESCRIÇÃO DO PROJETO
================================================================================

O **SmartVet** é um sistema web desenvolvido para auxiliar clínicas veterinárias no
gerenciamento de animais, prontuários e registros de vacinação, oferecendo uma
interface moderna, responsiva e preparada com **Dark Mode**.

O projeto foi construído utilizando **Spring Boot**, **H2 Database**, **HTML**, **CSS**,
**JavaScript** e **Bootstrap**, seguindo o padrão MVC, com organização modular e
separação clara entre controller, service, repository e model.

O objetivo principal do SmartVet é fornecer um sistema leve, rápido e fácil de
usar dentro de clínicas veterinárias, permitindo:

- 📌 Cadastro de Animais  
- 📌 Registro de Prontuários  
- 📌 Gerenciamento de Vacinas  
- 📌 Interface moderna com suporte a Dark Mode  
- 📌 Acesso rápido e responsivo via navegador  

================================================================================
                       ARQUITETURA E ESTRUTURA DO SISTEMA
================================================================================

O SmartVet segue a estrutura clássica de um projeto Spring Boot:

📁 **src/main/java/com/healthpet/veterinaria**
- controller/  
- service/  
- repository/  
- model/  
- enums/  
- exception/  
- VeterinariaApplication.java  

📁 **src/main/resources**
- static/  
  - css/style.css  
  - js/app.js  
  - index.html  
- application.properties  

📁 **Banco de dados**
- H2 Database (memória ou arquivo .mv.db)
- Console disponível em:  
  http://localhost:8080/h2-console

================================================================================
                         TECNOLOGIAS E FERRAMENTAS USADAS
================================================================================

✓ **Backend:**  
- Java 17  
- Spring Boot 3.x  
- Spring Data JPA  
- H2 Database  
- Hibernate ORM  
- Maven  

✓ **Frontend:**  
- HTML5  
- CSS3 (customização + dark mode)  
- JavaScript  
- Bootstrap 5  

✓ **Ferramentas:**  
- VS Code  
- Git e GitHub  
- H2 Console  
- Postman (testes opcionais)

================================================================================
                           FUNCIONALIDADES PRINCIPAIS
================================================================================

1. **Cadastro de Animais**  
   - Nome, espécie, idade, raça, pelagem e temperamento  
   - Validação automática  
   - Armazenamento no banco H2 via JPA  

2. **Gerenciamento de Vacinas**  
   - Cadastro de tipos de vacinas  
   - Datas de aplicação  
   - Controle de doses  

3. **Prontuários Veterinários**  
   - Registro de consultas  
   - Observações médicas  
   - Associação ao animal  

4. **Dark Mode**  
   - Ativado via JavaScript  
   - Personalização global via CSS e variáveis  
   - Efeito glassmorphism  

5. **Interface Responsiva**  
   - Utilizando Bootstrap  
   - Layout adaptado para mobile, tablet e desktop  

================================================================================
                               DETALHAMENTO TÉCNICO
================================================================================

📌 **Spring Boot – Controllers**  
Cada funcionalidade possui um controller dedicado:

- AnimalController  
- VacinaController  
- ProntuarioController  

Todos seguem o padrão REST, utilizando:

- @GetMapping  
- @PostMapping  
- @PutMapping  
- @DeleteMapping  

📌 **Service Layer**  
Implementa as regras de negócio:

- Processamento de dados  
- Validação  
- Tratamento de exceções  

📌 **Repository Layer**  
Camada de persistência usando Spring Data JPA:

- findAll()  
- findById()  
- save()  
- deleteById()  

📌 **Model Layer**  
Entidades do sistema:

- Animal  
- Prontuario  
- Vacina  

Incluem construtores, getters/setters e validações.

================================================================================
                               BANCO DE DADOS H2
================================================================================

O projeto utiliza H2 Database, permitindo rodar sem instalação externa.

Acesso ao console:
http://localhost:8080/h2-console

Configuração padrão:
jdbc:h2:mem:testdb
user: sa
password:


O banco é criado automaticamente pelas entidades JPA.

================================================================================
                      INTERFACE FRONTEND E EXPERIÊNCIA DO USUÁRIO
================================================================================

📌 **Bootstrap 5**  
Utilizado para:

- Grid responsivo  
- Cards e containers  
- Modais e botões  

📌 **Dark Mode**  
Implementado via:

- Classe `.dark`  
- Variáveis CSS  
- Função JS responsiva  

📌 **CSS Personalizado**  
Inclui:

- Glassmorphism  
- Animações suaves  
- Layout moderno  

📌 **index.html**  
Página inicial com:

- Navegação  
- Cards de funcionalidades  
- Área central de conteúdo  

================================================================================
                       INSTRUÇÕES DE INSTALAÇÃO E EXECUÇÃO
================================================================================

1. Baixar ou clonar o repositório:
git clone https://github.com/SEU_USUARIO/SEU_REPOSITORIO.git

2. Abrir no VS Code ou IntelliJ

3. Rodar o projeto com Maven:
mvn spring-boot:run

4. Acessar no navegador:
http://localhost:8080

5. Acessar o console do H2 se necessário:
http://localhost:8080/h2-console


================================================================================
                          TROUBLESHOOTING (SOLUÇÃO DE PROBLEMAS)
================================================================================

❗ **CSS/JS não carregam**  
- Limpar cache do navegador  
- Verificar pasta /static  

❗ **Erro 404 nas rotas**  
- Verifique se o servidor está executando  
- Rotas REST não substituem as páginas HTML  

❗ **H2 não abre**  
- Certifique-se que o console está habilitado no application.properties  

❗ **Erro de CORS (caso teste via Postman)**  
- Configurar WebConfig (se necessário)

================================================================================
                                  LICENÇA
================================================================================

Este projeto está licenciado sob a **MIT License**.  
Permite uso, modificação e distribuição livre mediante citação do autor.

================================================================================
                                   AUTOR
================================================================================

**Felipe Brito dos Santos Rocha**  
Desenvolvedor do projeto SmartVet  
GitHub: *adicione seu link aqui*  

================================================================================
                               AGRADECIMENTOS
================================================================================

- Professores e orientadores  
- Colegas que auxiliaram no desenvolvimento  
- Comunidade Java/Spring Boot  
- Ferramentas open-source utilizadas  

================================================================================
                                     FIM
================================================================================


