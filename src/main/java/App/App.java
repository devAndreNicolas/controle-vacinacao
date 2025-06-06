package App;

import View.Components.MainFrame;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        // Configura o Look and Feel do FlatLaf (deve ser a primeira coisa a fazer na main)
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("Falha ao carregar o Look and Feel: " + e.getMessage());
        }

        // Garante que a UI seja criada e manipulada na Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
}