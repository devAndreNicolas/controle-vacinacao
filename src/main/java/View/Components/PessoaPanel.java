package View.Components;

import Controller.PessoaController;
import Model.Pessoa;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class PessoaPanel extends JPanel {

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
                Arrays.asList("ID", "Nome", "CPF", "Data de Nascimento"),
                p -> new Object[]{
                        p.getId(),
                        p.getNome(),
                        p.getCpf(),
                        p.getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                }
        );
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);
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

    private void refreshTable() {
        List<Pessoa> pessoas = controller.listarPessoas();
        tableModel.setItems(pessoas);
    }

    private JFrame getParentFrame() {
        return (JFrame) SwingUtilities.getWindowAncestor(this);
    }
}