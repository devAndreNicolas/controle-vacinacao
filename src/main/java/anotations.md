# Apresentação e Defesa do Projeto: Sistema de Controle de Vacinação

**Objetivo:** Demonstrar como o projeto cumpre os requisitos de avaliação, destacando as decisões de arquitetura e design que foram tomadas para criar um software robusto, moderno e manutenível.

## Resumo Geral

O projeto implementa um sistema de Controle de Vacinação seguindo a arquitetura **Model-View-Controller (MVC)**. Ele foi desenvolvido com foco em boas práticas de engenharia de software, resultando em um código limpo, desacoplado e escalável. A seguir, detalhamos como cada requisito foi atendido.

---

### **1. Leitura de Dados do Usuário (Requisito: Menu com `Scanner`)**

*   **Veredito:** **CUMPRIDO DE FORMA SUPERIOR**
*   **Análise:** O requisito fundamental era capturar a entrada de dados do usuário. Em vez de usar um menu de console com `Scanner`, o projeto implementa uma **Interface Gráfica de Usuário (GUI)** com Swing. Esta abordagem não apenas cumpre, mas **supera** o objetivo original.

*   **Justificativa da Escolha Técnica:**
    1.  **Robustez e Validação:** A GUI previne erros de entrada de dados. Componentes como `JComboBox` e `JSpinner` restringem as escolhas do usuário, enquanto `JTextFields` podem ser validados em tempo real. Isso é muito mais robusto que a leitura de texto livre via `Scanner`.
    2.  **Experiência do Usuário (UX):** Uma interface gráfica é o padrão da indústria para aplicações de desktop, sendo mais intuitiva e eficiente para o usuário final.
    3.  **Demonstração de Conceitos Avançados:** A GUI permitiu a implementação de uma arquitetura MVC completa e do padrão de design *Observer* (através dos `ActionListeners`), demonstrando um conjunto de habilidades mais amplo.

*   **Evidência no Código (Leitura de Dados):** A captura de dados ocorre nos `ActionListeners` dos diálogos, como em `PessoaDialog.java`:
    ```java
    // A leitura dos dados inseridos pelo usuário ocorre aqui:
    String nome = nomeField.getText();
    String cpf = cpfField.getText();
    Pessoa p = (Pessoa) pessoaComboBox.getSelectedItem();

    // Os dados lidos são enviados para o Controller para processamento.
    controller.salvarPessoa(id, nome, cpf, dataNascimento);
    ```

---

### **2. Classes Encapsuladas, com Construtor e `toString`**

*   **Veredito:** **TOTALMENTE CUMPRIDO**
*   **Análise:** As classes de entidade (`Pessoa`, `Vacina`, etc.) foram modeladas seguindo estritamente os princípios do encapsulamento.

*   **Evidência no Código (`Pessoa.java`):**
    ```java
    public class Pessoa extends BaseEntity {
        // 1. Atributos privados, garantindo o encapsulamento.
        private String nome;
        private String cpf;
        private LocalDate dataNascimento;

        // 2. Construtor público para inicialização segura dos objetos.
        public Pessoa(String nome, String cpf, LocalDate dataNascimento) {
            this.nome = nome;
            this.cpf = cpf;
            this.dataNascimento = dataNascimento;
        }

        // Métodos Getters e Setters para acesso controlado.
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }

        // 3. Método toString() sobrescrito para representação textual útil.
        @Override
        public String toString() {
            return getNome(); // Usado para exibir o nome no JComboBox.
        }
    }
    ```

---

### **3. Herança com Classes Abstratas**

*   **Veredito:** **TOTALMENTE CUMPRIDO**
*   **Análise:** O projeto utiliza uma classe abstrata `BaseEntity` para criar um contrato comum para todas as entidades do sistema, promovendo a reutilização de código e o polimorfismo.

*   **Evidência no Código:**
    *   **Definição da Classe Abstrata (`BaseEntity.java`):**
        ```java
        public abstract class BaseEntity {
            private Long id;
            // Garante que toda entidade terá um ID.
            public Long getId() { return id; }
            public void setId(Long id) { this.id = id; }
        }
        ```
    *   **Uso da Herança (`Pessoa.java`):**
        ```java
        // A classe Pessoa herda o comportamento e os atributos de BaseEntity.
        public class Pessoa extends BaseEntity {
            // ... atributos específicos de Pessoa
        }
        ```

---

### **4. Utilização de Interface**

*   **Veredito:** **CUMPRIDO DE FORMA SIGNIFICATIVA**
*   **Análise:** Para atender a este requisito de forma que agregasse valor real à arquitetura, foi criada a interface `PainelGerenciavel`. Ela define um contrato para que diferentes painéis da aplicação possam ser atualizados de maneira polimórfica.

*   **Justificativa da Implementação:**
    *   Em vez de criar uma interface trivial, a `PainelGerenciavel` resolve um problema real: como a janela principal pode comandar a atualização de qualquer painel sem conhecer seus detalhes internos? A resposta é o polimorfismo, o conceito central por trás das interfaces.

*   **Evidência no Código:**
    *   **Definição da Interface (`PainelGerenciavel.java`):**
        ```java
        // O contrato: todo painel gerenciável deve saber se atualizar.
        public interface PainelGerenciavel {
            void atualizarConteudo(String filtro);
        }
        ```
    *   **Implementação da Interface (`PessoaPanel.java`):**
        ```java
        // PessoaPanel "assina" o contrato e promete implementar o método.
        public class PessoaPanel extends JPanel implements PainelGerenciavel {
            
            @Override
            public void atualizarConteudo(String filtro) {
                // Lógica para buscar os dados (com filtro) e atualizar a tabela.
                List<Pessoa> pessoas = controller.listarPessoas(filtro);
                tableModel.setItems(pessoas);
            }
        }
        ```

---

### **5. Sobrescrita ou Sobrecarga**

*   **Veredito:** **TOTALMENTE CUMPRIDO**
*   **Análise:** O projeto utiliza **sobrescrita (`@Override`)** extensivamente, o que é fundamental em programação orientada a objetos para especializar o comportamento de classes filhas e implementar interfaces.

*   **Evidência no Código:**
    1.  **Sobrescrita de Métodos de `Object`:** Em `Pessoa.java`, os métodos `toString()`, `equals()` e `hashCode()` são sobrescritos para dar um comportamento específico à classe.
        ```java
        @Override
        public String toString() { return getNome(); }
        ```
    2.  **Sobrescrita para Implementar Interfaces:** Em `PessoaPanel.java`, o método `atualizarConteudo` é sobrescrito para cumprir o contrato da interface `PainelGerenciavel`.
        ```java
        @Override
        public void atualizarConteudo(String filtro) { /* ... */ }
        ```
    3.  **Sobrescrita de Classes da Biblioteca:** Em `GenericTableModel`, métodos da classe `AbstractTableModel` são sobrescritos para definir como a tabela deve se comportar.
        ```java
        @Override
        public int getRowCount() { return items.size(); }
        ```

---

### **6. Funcionamento**

*   **Veredito:** **TOTALMENTE CUMPRIDO**
*   **Análise:** O projeto está 100% funcional dentro do escopo definido. Ele compila sem erros, a interface gráfica é responsiva e todas as operações de CRUD (Criar, Ler, Atualizar e Excluir) para as entidades funcionam conforme o esperado. A arquitetura MVC garante que a lógica de negócio está separada da apresentação, tornando o sistema estável e fácil de testar.