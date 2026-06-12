import javax.swing.*;
import java.awt.*;

public class ToolIcons extends JToolBar {

    public ToolIcons(MainWindow mainwindow) {
        setFloatable(false);
        setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        JButton NewButton  = CreateIconButton("📄", "New File");
        JButton OpenButton = CreateIconButton("📂", "Open File");
        JButton SaveButton = CreateIconButton("💾", "Save File");
        JButton RunButton = CreateIconButton("▶", "Run");
        JButton UndoButton = CreateIconButton("↩", "Undo");
        JButton RedoButton = CreateIconButton("↪", "Redo");
        JButton SearchButton = CreateIconButton("🔍", "Find & Replace");

//        Focus Timer Buttons

        JButton StartTimerButton = CreateIconButton("🍅", "Start Focus Timer");
        JButton PauseTimerButton = CreateIconButton("⏸", "Pause Focus Timer");
        JButton ResetTimerButton  = CreateIconButton("⏹", "Reset Focus Timer");

//        Code Shot Button

        JButton SnapBtn = CreateIconButton("📸", "Code Shot");
        SnapBtn.addActionListener(e -> mainwindow.TakeCodeshot());

//        ===== Action Handlers =====

        NewButton.addActionListener(e-> mainwindow.HandlingNew());          // New
        OpenButton.addActionListener(e -> mainwindow.HandlingOpen());       // Open
        SaveButton.addActionListener(e -> mainwindow.HandlingSave());       // Save
        RunButton.addActionListener(e -> mainwindow.HandlingRun());         // Run
        UndoButton.addActionListener(e -> mainwindow.HandlingUndo());       // Undo
        RedoButton.addActionListener(e -> mainwindow.HandlingRedo());       // Redo
        SearchButton.addActionListener(e -> mainwindow.HandlingSearch());   // Search
        StartTimerButton.addActionListener(e -> mainwindow.StartTimer());   // Start (Timer)
        PauseTimerButton.addActionListener(e -> mainwindow.PauseTimer());   // Pause (Timer)
        ResetTimerButton.addActionListener(e -> mainwindow.ResetTimer());   // Reset (Timer)

//         Only run button gets special color

        RunButton.setForeground(new Color(80, 200, 80));
        RunButton.setFont(new Font("Segoe UI Symbol", Font.BOLD, 16));

//        Adding buttons to the Tool Icons (Bar)

        add(NewButton);
        add(OpenButton);
        add(SaveButton);

//        ===== Separator =====

        addSeparator();

        add(RunButton);

//        ===== Separator =====

        addSeparator();

        add(UndoButton);
        add(RedoButton);

//        ===== Separator =====

        addSeparator();

        add(SearchButton);

//        ===== Separator =====

        addSeparator();

        add(StartTimerButton);
        add(PauseTimerButton);
        add(ResetTimerButton);

//        ===== Separator =====

        addSeparator();

        add(SnapBtn);
    }

    private JButton CreateIconButton(String icon, String tooltip) {
        JButton button = new JButton(icon);
        button.setToolTipText(tooltip);
        button.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
}