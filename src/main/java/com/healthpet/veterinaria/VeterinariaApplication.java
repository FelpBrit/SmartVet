package com.healthpet.veterinaria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * VeterinariaApplication - CLASSE PRINCIPAL
 * 
 * Ponto de entrada da aplicação Spring Boot.
 * Esta classe inicia toda a aplicação, incluindo:
 * - Servidor web (Tomcat embutido)
 * - Configuração do banco de dados
 * - Controllers, Services e Repositories
 * - APIs REST
 * 
 * @SpringBootApplication - Combina várias anotações:
 *   - @Configuration: Marca como classe de configuração
 *   - @EnableAutoConfiguration: Habilita configuração automática
 *   - @ComponentScan: Escaneia pacotes buscando componentes
 * 
 * Como executar:
 * 1. Pelo VSCode: Clique em "Run" acima do método main()
 * 2. Pelo terminal: mvn spring-boot:run
 * 3. Pelo Maven: ./mvnw spring-boot:run
 * 
 * Após iniciar, acesse:
 * - Frontend: http://localhost:8080
 * - API: http://localhost:8080/api/animais
 * - Console H2: http://localhost:8080/h2-console
 * 
 * @author Felipe Brito
 * @version 2.0
 */
@SpringBootApplication
public class VeterinariaApplication {

    /**
     * Método main - inicia a aplicação Spring Boot
     * 
     * @param args argumentos de linha de comando
     */
    public static void main(String[] args) {
        // Inicia a aplicação Spring Boot
        SpringApplication.run(VeterinariaApplication.class, args);
        
        // Mensagem de boas-vindas no console
        System.out.println("\n");
        System.out.println("=".repeat(60));
        System.out.println("🐾 HealthPet Sistema Veterinária iniciado com sucesso! 🐾");
        System.out.println("=".repeat(60));
        System.out.println("\n📱 Acesse a aplicação:");
        System.out.println("   → Frontend: http://localhost:8080");
        System.out.println("   → API REST: http://localhost:8080/api/animais");
        System.out.println("   → Console H2: http://localhost:8080/h2-console");
        System.out.println("\n💾 Banco de dados H2 (em memória):");
        System.out.println("   → JDBC URL: jdbc:h2:mem:veterinariadb");
        System.out.println("   → Username: sa");
        System.out.println("   → Password: (deixe vazio)");
        System.out.println("\n📚 Documentação da API:");
        System.out.println("   → GET    /api/animais          - Lista todos");
        System.out.println("   → GET    /api/animais/{id}     - Busca por ID");
        System.out.println("   → POST   /api/animais/cachorro - Cadastra cachorro");
        System.out.println("   → POST   /api/animais/gato     - Cadastra gato");
        System.out.println("   → PUT    /api/animais/{id}     - Atualiza animal");
        System.out.println("   → DELETE /api/animais/{id}     - Remove animal");
        System.out.println("\n" + "=".repeat(60));
        System.out.println("✨ Pressione Ctrl+C para parar o servidor");
        System.out.println("=".repeat(60) + "\n");
    }
}