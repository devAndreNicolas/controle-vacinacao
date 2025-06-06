package View.Components;

import Controller.PessoaController;
import Model.Pessoa;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.awt.event.KeyAdapter; // Importe KeyAdapter para teste de foco
import java.awt.event.KeyEvent;    // Importe KeyEvent para teste de foco


public class PessoaDialog extends JDialog {

    private final PessoaController controller;
    private final Pessoa pessoaParaEditar;

    private JTextField nomeField;
    private JTextField cpfField;
    private JTextField dataNascimentoField;

    public PessoaDialog(JFrame parent, PessoaController controller, Pessoa pessoaParaEditar) {
        super(parent, true); // Torna o diálogo modal
        this.controller = controller;
        this.pessoaParaEditar = pessoaParaEditar;

        setTitle(pessoaParaEditar == null ? "Adicionar Pessoa" : "Editar Pessoa");
        setLayout(new BorderLayout(10, 10)); // Layout principal do diálogo
        // setSize(400, 250); // Comente ou remova esta linha. Usaremos pack()
        setLocationRelativeTo(parent); // Centraliza o diálogo em relação à janela pai

        // Adiciona os painéis ao diálogo
        add(createFormPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        // Preenche o formulário se estiver em modo de edição
        if (pessoaParaEditar != null) {
            populateForm();
        }

        // Ajusta o tamanho do diálogo para o tamanho preferencial dos componentes
        pack();

        // Tenta garantir que o campo de nome receba o foco após o diálogo ser visível
        SwingUtilities.invokeLater(() -> nomeField.requestFocusInWindow());

        /*
         * Bloco de código para depuração:
         * Se o problema persistir, descomente o KeyListener abaixo
         * para verificar se as teclas estão sendo capturadas,
         * mesmo que o texto não esteja visível.
         */
        // nomeField.addKeyListener(new KeyAdapter() {
        //     @Override
        //     public void keyTyped(KeyEvent e) {
        //         System.out.println("Digitou no nomeField: " + e.getKeyChar());
        //     }
        // });
        // cpfField.addKeyListener(new KeyAdapter() {
        //     @Override
        //     public void keyTyped(KeyEvent e) {
        //         System.out.println("Digitou no cpfField: " + e.getKeyChar());
        //     }
        // });
        // dataNascimentoField.addKeyListener(new KeyAdapter() {
        //     @Override
        //     public void keyTyped(KeyEvent e) {
        //         System.out.println("Digitou no dataNascimentoField: " + e.getKeyChar());
        //     }
        // });
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout()); // Usa GridBagLayout para controle preciso
        GridBagConstraints gbc = new GridBagConstraints(); // Objeto para configurar as restrições
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // Margem interna

        gbc.insets = new Insets(5, 5, 5, 5); // Espaçamento entre os componentes
        gbc.fill = GridBagConstraints.HORIZONTAL; // Faz o componente preencher o espaço horizontalmente

        // Inicialização dos campos de texto
        nomeField = new JTextField(20); // Sugestão de largura de 20 colunas
        cpfField = new JTextField(20);
        dataNascimentoField = new JTextField(20);
        dataNascimentoField.setToolTipText("Use o formato dd/MM/yyyy");

        // --- Adição dos componentes ao painel usando GridBagConstraints ---

        // Label Nome
        gbc.gridx = 0; // Coluna 0
        gbc.gridy = 0; // Linha 0
        gbc.weightx = 0.0; // Labels não devem se expandir horizontalmente
        panel.add(new JLabel("Nome:"), gbc);

        // Campo Nome
        gbc.gridx = 1; // Coluna 1
        gbc.gridy = 0; // Linha 0
        gbc.weightx = 1.0; // *** IMPORTANTE: Permite que o campo se expanda horizontalmente ***
        panel.add(nomeField, gbc);

        // Label CPF
        gbc.gridx = 0; // Coluna 0
        gbc.gridy = 1; // Linha 1
        gbc.weightx = 0.0; // Labels não devem se expandir horizontalmente
        panel.add(new JLabel("CPF:"), gbc);

        // Campo CPF
        gbc.gridx = 1; // Coluna 1
        gbc.gridy = 1; // Linha 1
        gbc.weightx = 1.0; // *** IMPORTANTE: Permite que o campo se expanda horizontalmente ***
        panel.add(cpfField, gbc);

        // Label Data de Nascimento
        gbc.gridx = 0; // Coluna 0
        gbc.gridy = 2; // Linha 2
        gbc.weightx = 0.0; // Labels não devem se expandir horizontalmente
        panel.add(new JLabel("Data Nasc. (dd/MM/yyyy):"), gbc);

        // Campo Data de Nascimento
        gbc.gridx = 1; // Coluna 1
        gbc.gridy = 2; // Linha 2
        gbc.weightx = 1.0; // *** IMPORTANTE: Permite que o campo se expanda horizontalmente ***
        panel.add(dataNascimentoField, gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Alinha os botões à direita
        JButton saveButton = new JButton("Salvar");
        JButton cancelButton = new JButton("Cancelar");

        saveButton.addActionListener(e -> save()); // Ação para o botão Salvar
        cancelButton.addActionListener(e -> dispose()); // Fecha o diálogo ao cancelar

        panel.add(saveButton);
        panel.add(cancelButton);
        return panel;
    }

    // Preenche os campos com os dados da pessoa se estiver em modo de edição
    private void populateForm() {
        nomeField.setText(pessoaParaEditar.getNome());
        cpfField.setText(pessoaParaEditar.getCpf());
        dataNascimentoField.setText(pessoaParaEditar.getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    private void save() {
        String nome = nomeField.getText();
        String cpf = cpfField.getText();
        String dataStr = dataNascimentoField.getText();

        // Validação de campos obrigatórios
        if (nome.isBlank() || cpf.isBlank() || dataStr.isBlank()) {
            JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Tenta fazer o parse da data de nascimento
            LocalDate dataNascimento = LocalDate.parse(dataStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            Long id = (pessoaParaEditar != null) ? pessoaParaEditar.getId() : null; // Usa o ID existente se for edição
            controller.salvarPessoa(id, nome, cpf, dataNascimento); // Chama o controller para salvar
            dispose(); // Fecha o diálogo após salvar
        } catch (DateTimeParseException ex) {
            // Erro se o formato da data estiver incorreto
            JOptionPane.showMessageDialog(this, "Formato de data inválido. Use dd/MM/yyyy.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }
}