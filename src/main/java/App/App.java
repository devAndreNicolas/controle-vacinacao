package App;

import View.Components.MainFrame;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatMacLightLaf());
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