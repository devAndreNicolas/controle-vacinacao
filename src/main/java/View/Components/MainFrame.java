package View.Components;
import javax.swing.*;
import java.awt.BorderLayout;

/**
 * Janela principal da aplicação.
 * Utiliza um JTabbedPane para organizar as diferentes seções do sistema.
 */
public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Sistema de Controle de Vacinação");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Aba 1: Registros de Vacinação
        VacinacaoPanel vacinacaoPanel = new VacinacaoPanel();
        tabbedPane.addTab("Registros de Vacinação", vacinacaoPanel);

        // Aba 2: Cadastro de Pessoas
        PessoaPanel pessoaPanel = new PessoaPanel();
        tabbedPane.addTab("Pessoas", pessoaPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }
}