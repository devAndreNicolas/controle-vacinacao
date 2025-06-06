package View.Components;

import Controller.VacinacaoController;
import Model.Pessoa;
import Model.Vacina;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class VacinacaoDialog extends JDialog {

    private final VacinacaoController controller;
    private JComboBox<Pessoa> pessoaComboBox;
    private JComboBox<Vacina> vacinaComboBox;
    private JSpinner doseSpinner;

    public VacinacaoDialog(JFrame parent, VacinacaoController controller) {
        super(parent, "Registrar Vacinação", true);
        this.controller = controller;

        setLayout(new BorderLayout(10, 10));
        setSize(400, 250);
        setLocationRelativeTo(parent);

        add(createFormPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Linha 0: Label Pessoa
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(new JLabel("Pessoa:"), gbc);

        // Linha 0: ComboBox Pessoa
        gbc.gridx = 1;
        gbc.weightx = 1.0; // Permite que o componente estique horizontalmente
        gbc.anchor = GridBagConstraints.LINE_START;
        pessoaComboBox = new JComboBox<>(controller.listarPessoas().toArray(new Pessoa[0]));
        panel.add(pessoaComboBox, gbc);

        // Linha 1: Label Vacina
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(new JLabel("Vacina:"), gbc);

        // Linha 1: ComboBox Vacina
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.LINE_START;
        vacinaComboBox = new JComboBox<>(controller.listarVacinas().toArray(new Vacina[0]));
        panel.add(vacinaComboBox, gbc);

        // Linha 2: Label Dose
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(new JLabel("Dose:"), gbc);

        // Linha 2: Spinner Dose
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.LINE_START;
        doseSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
        panel.add(doseSpinner, gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        JButton saveButton = new JButton("Salvar");
        JButton cancelButton = new JButton("Cancelar");

        saveButton.addActionListener(e -> {
            Pessoa p = (Pessoa) pessoaComboBox.getSelectedItem();
            Vacina v = (Vacina) vacinaComboBox.getSelectedItem();

            if (p == null || v == null) {
                JOptionPane.showMessageDialog(this, "Por favor, selecione uma pessoa e uma vacina.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int dose = (int) doseSpinner.getValue();

            controller.registrarVacinacao(p, v, LocalDate.now(), dose);

            JOptionPane.showMessageDialog(this, "Registro salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });

        cancelButton.addActionListener(e -> dispose());

        panel.add(saveButton);
        panel.add(cancelButton);
        return panel;
    }
}