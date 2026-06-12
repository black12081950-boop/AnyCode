import javax.swing.*;
import java.awt.*;

public class ClosingTab extends JPanel {

    public ClosingTab(String title, JTabbedPane TabbedPane , MainWindow mainWindow) {
        setOpaque(false);           // transparent background
        setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

        // Tab title label

        JLabel TitleLabel = new JLabel(title + "  ");
        TitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        // X button

        JButton CloseButton = new JButton("×");
        CloseButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        CloseButton.setFocusPainted(false);
        CloseButton.setContentAreaFilled(false); // no background
        CloseButton.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
        CloseButton.setCursor(new Cursor(Cursor.HAND_CURSOR));


        CloseButton.addActionListener(e -> {
            int index = TabbedPane.indexOfTabComponent(this);
            if (index != -1) {
                TabbedPane.removeTabAt(index);
                mainWindow.RemoveTab(index);
            }
        });

        add(TitleLabel);
        add(CloseButton);
    }
}