package br.com.sispoli;

import br.com.sispoli.controller.MenuPrincipalController;
import br.com.sispoli.view.MenuPrincipalView;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Aparência nativa + otimizações de renderização
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                UIManager.put("Button.defaultButtonFollowsFocus", true);
                UIManager.put("ScrollBar.thumbTrackAmount", 16);
            } catch (Exception ignored) {}

            MenuPrincipalView view = new MenuPrincipalView();
            new MenuPrincipalController(view);
            view.setVisible(true);
        });
    }
}