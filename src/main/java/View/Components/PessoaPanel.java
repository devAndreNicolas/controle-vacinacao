package View.Components;

import Controller.PessoaController;
import Model.GenericTableModel;
import Model.Pessoa;

import javax.swing.*;
import javax.swing.table.TableColumnModel; // Importe TableColumnModel
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class PessoaPanel extends JPanel implements PainelGerenciavel {

    private final PessoaController controller;
    private JTable table;
    private GenericTableModel<Pessoa> tableModel;

    public PessoaPanel() {
        this.controller = new PessoaController();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        setupTable();
        setupButtons();
        refreshTable();
    }

    private void setupTable() {
        tableModel = new GenericTableModel<>(
                // Mantemos "ID" na lista de cabeçalhos para mapear o primeiro dado corretamente.
                Arrays.asList("ID", "Nome", "CPF", "Data de Nascimento"),
                p -> new Object[]{
                        p.getId(), // O ID continua sendo o primeiro elemento dos dados
                        p.getNome(),
                        p.getCpf(),
                        p.getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                }
        );
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // --- Adição CRÍTICA: Oculta a coluna do ID após a criação da tabela ---
        hideIdColumn();
    }

    /**
     * Oculta a coluna "ID" da JTable.
     * A coluna é removida da visualização, mas seus dados permanecem no TableModel,
     * permitindo o acesso programático (ex: para exclusão ou edição).
     */
    private void hideIdColumn() {
        // Verifica se a tabela tem colunas antes de tentar removê-las
        if (table.getColumnModel().getColumnCount() > 0) {
            TableColumnModel columnModel = table.getColumnModel();
            // A coluna "ID" é a primeira, no índice 0
            columnModel.removeColumn(columnModel.getColumn(0));
        }
    }

    private void setupButtons() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Adicionar");
        JButton editButton = new JButton("Editar");
        JButton deleteButton = new JButton("Excluir");

        addButton.addActionListener(e -> {
            PessoaDialog dialog = new PessoaDialog(getParentFrame(), controller, null);
            dialog.setVisible(true);
            refreshTable();
        });

        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                // O ID ainda é acessado pela coluna 0 do tableModel, mesmo que oculta
                Long id = (Long) tableModel.getValueAt(selectedRow, 0);
                Pessoa pessoaParaEditar = controller.listarPessoas().stream()
                        .filter(p -> p.getId().equals(id)).findFirst().orElse(null);

                if (pessoaParaEditar != null) {
                    PessoaDialog dialog = new PessoaDialog(getParentFrame(), controller, pessoaParaEditar);
                    dialog.setVisible(true);
                    refreshTable();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma pessoa para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir esta pessoa?", "Confirmação", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    // O ID ainda é acessado pela coluna 0 do tableModel, mesmo que oculta
                    Long id = (Long) tableModel.getValueAt(selectedRow, 0);
                    try {
                        controller.excluirPessoa(id);
                        refreshTable();
                    } catch (IllegalStateException ex) {
                        JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma pessoa para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    public void refreshTable() {
        List<Pessoa> pessoas = controller.listarPessoas();
        tableModel.setItems(pessoas);
    }

    private JFrame getParentFrame() {
        return (JFrame) SwingUtilities.getWindowAncestor(this);
    }
}