package View.Components;

import Controller.VacinacaoController;
import Model.Vacinacao;

import javax.swing.*;
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
            VacinacaoDialog dialog = new VacinacaoDialog(getParentFrame(), controller);
            dialog.setVisible(true);
            refreshTable();
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir este registro?", "Confirmação", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
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