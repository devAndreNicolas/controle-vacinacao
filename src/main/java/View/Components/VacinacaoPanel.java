package View.Components;

import Controller.VacinacaoController;
import Model.Vacinacao;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class VacinacaoPanel extends JPanel {

    private final VacinacaoController controller;
    private JTable table;
    private GenericTableModel<Vacinacao> tableModel;

    public VacinacaoPanel() {
        this.controller = new VacinacaoController();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        setupTable();
        setupButtons();
        refreshTable();
    }

    private void setupTable() {
        tableModel = new GenericTableModel<>(
                // Mantemos o "ID" aqui para que o modelo da tabela ainda o tenha
                Arrays.asList("ID", "Pessoa", "CPF", "Vacina", "Data", "Dose"),
                v -> new Object[]{
                        v.getId(),
                        v.getPessoa().getNome(),
                        v.getPessoa().getCpf(),
                        v.getVacina().getNome(),
                        v.getDataAplicacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        v.getDose()
                }
        );
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // --- Adição CRÍTICA: Oculta a coluna do ID ---
        hideIdColumn();
    }

    private void hideIdColumn() {
        // Obtém o modelo de colunas da tabela
        TableColumnModel columnModel = table.getColumnModel();
        // Remove a coluna na posição 0 (que é o "ID")
        // Note que removeColumn remove a coluna da *visualização* da tabela,
        // mas ela ainda existe no TableModel subjacente para acesso programático.
        columnModel.removeColumn(columnModel.getColumn(0));
    }


    private void setupButtons() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Adicionar Registro");
        JButton deleteButton = new JButton("Excluir Registro");

        addButton.addActionListener(e -> {
            if (controller.listarPessoas().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Cadastre uma pessoa primeiro na aba 'Pessoas'.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Não precisa passar o ID para o diálogo de adição, pois é um novo registro
            VacinacaoDialog dialog = new VacinacaoDialog(getParentFrame(), controller);
            dialog.setVisible(true);
            refreshTable();
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir este registro?", "Confirmação", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    // Obtém o ID da linha selecionada.
                    // Mesmo que a coluna ID esteja oculta, ela ainda é a coluna 0 no tableModel.
                    Long id = (Long) tableModel.getValueAt(selectedRow, 0);
                    controller.excluirVacinacao(id);
                    refreshTable();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um registro para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void refreshTable() {
        List<Vacinacao> vacinacoes = controller.listarVacinacoes();
        tableModel.setItems(vacinacoes);
    }

    private JFrame getParentFrame() {
        return (JFrame) SwingUtilities.getWindowAncestor(this);
    }
}