import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;

public class MainWindow {

    private JFrame frame;
    private JTabbedPane TabbedPane;
    private OutputPanel outputPanel;
    private CodeExecuter codeExecuter = new CodeExecuter();
    private StatusBar statusBar;
    private ArrayList<EditorTab> EditorTabs = new ArrayList<>();
    private FocusTimer focusTimer;

//    Constructor of MainWindow

    public MainWindow() {

        frame = new JFrame("AnyCode");  //         Title of the main window
        frame.setSize(1200, 800);  //        Size of the window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // JFrame method used for closing the screen byh pressing the cross button on top right
        frame.setLocationRelativeTo(null);     //         Centered the window
        frame.getContentPane().setBackground(new Color(30, 30, 30)); // (30,30,30) is dark grayish color
        frame.setLayout(new BorderLayout());

//        ====== Building MenuBar =====

        frame.setJMenuBar(BuildMenuBar());

//        Icon for AnyCode

        try {
            java.net.URL iconUrl = getClass().getResource("/AnyCode_icon.png");
            if (iconUrl != null) {
                frame.setIconImage(new ImageIcon(iconUrl).getImage());
            }
        } catch (Exception e) {
            // Skip if not found
        }

//        ===== Status Bar =====

        statusBar = new StatusBar();

//        ===== Focus Timer =====

        focusTimer = new FocusTimer(statusBar , frame);

//        ====== TabedPane ======

        TabbedPane = new JTabbedPane();
        TabbedPane.addChangeListener(e -> {
            EditorTab tab = getCurrentTab();
            if (tab != null && tab.getCurrentFile() != null) {
                statusBar.UpdateFile(tab.getCurrentFile().getName());
                frame.setTitle("AnyCode — " + tab.getCurrentFile().getName());
            } else {
                statusBar.UpdateFile("No File");
                frame.setTitle("AnyCode");
            }
        });
        UIManager.put("TabbedPane.selected", new Color(30, 30, 30));        // selected tab background
        UIManager.put("TabbedPane.background", new Color(37, 37, 37));      // unselected tab background
        UIManager.put("TabbedPane.foreground", new Color(0, 0, 0));   // tab text color
        UIManager.put("TabbedPane.selectedForeground", new Color(0, 0, 0)); // selected tab text
        UIManager.put("TabbedPane.contentAreaColor", new Color(30, 30, 30));
        UIManager.put("TabbedPane.shadow", new Color(30, 30, 30));
        UIManager.put("TabbedPane.darkShadow", new Color(30, 30, 30));
        UIManager.put("TabbedPane.light", new Color(37, 37, 37));
        UIManager.put("TabbedPane.highlight", new Color(37, 37, 37));
        UIManager.put("TabbedPane.focus", new Color(30, 30, 30));
        TabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 13));

//        Open first empty tab as default

        AddNewTab("Untitled-1");

//        Creating Output Area

        outputPanel = new OutputPanel();
        JPanel BottomPanel = new JPanel(new BorderLayout());
        outputPanel.setVisible(false);

        BottomPanel.add(outputPanel , BorderLayout.CENTER);
        BottomPanel.add(statusBar , BorderLayout.SOUTH);

        frame.add(BottomPanel , BorderLayout.SOUTH);
        frame.add(TabbedPane, BorderLayout.CENTER);
        frame.add(new ToolIcons(this) , BorderLayout.NORTH);

//        Connecting the Sender Button

        outputPanel.getSenderButton().addActionListener(e -> {
            String input = outputPanel.getInputField().getText();
            codeExecuter.sendInput(input);
            outputPanel.getInputField().setText("");  // clear after sending
        });

        outputPanel.getInputField().addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    String input = outputPanel.getInputField().getText();
                    codeExecuter.sendInput(input);
                    outputPanel.getInputField().setText("");
                }
            }
        });
    }

//    Method of creating a new tab

    private void AddNewTab(String TabTitle) {
        EditorTab Tab = new EditorTab(TabTitle , statusBar);
        EditorTabs.add(Tab);

        JPanel tabPanel = new JPanel(new BorderLayout());
        tabPanel.setBackground(new Color(30, 30, 30));
        tabPanel.add(Tab.getScrollPane(), BorderLayout.CENTER);

        TabbedPane.addTab(TabTitle, Tab.getScrollPane());

        TabbedPane.setTabComponentAt(TabbedPane.getTabCount() - 1, new ClosingTab(TabTitle, TabbedPane , this));
        TabbedPane.setSelectedIndex(TabbedPane.getTabCount() - 1);
        TabbedPane.putClientProperty("Tab-" + (TabbedPane.getTabCount() - 1), Tab);
    }

//  Method of removing tab from arraylist

    public void RemoveTab(int index) {
        if (index >= 0 && index < EditorTabs.size()) {
            EditorTabs.remove(index);
        }
    }

//    Method of getting current Tab

    private EditorTab getCurrentTab() {
        int index = TabbedPane.getSelectedIndex();
        if (index == -1 || index >= EditorTabs.size()) {
            return null;
        }
        return EditorTabs.get(index);
    }

    public void StartTimer() {
        focusTimer.Start();
    }

    public void PauseTimer() {
        focusTimer.Pause();
    }

    public void ResetTimer()  {
        focusTimer.Stop();
    }

//    Method for taking Code Shot (pic)

    public void TakeCodeshot() {
        EditorTab tab = getCurrentTab();
        if (tab == null) return;

        String fileName = tab.getCurrentFile() != null ? tab.getCurrentFile().getName(): "Untitled.java";

        CodeShot.Take(tab.getCodeEditor(), fileName, frame);
    }

//    Method for creating MenuBar

    private JMenuBar BuildMenuBar() {

//      New MenuBar

        JMenuBar MenuBar = new JMenuBar();
        MenuBar.setBackground(new Color(37, 37, 37));
        MenuBar.setBorder(BorderFactory.createEmptyBorder());

//        Menu (File)

        JMenu FileMenu = CreateMenu("File");

        JMenuItem NewItem = CreateMenuItem("New");
        NewItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        NewItem.addActionListener(e -> HandlingNew());

        JMenuItem OpenItem = CreateMenuItem("Open");
        OpenItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        OpenItem.addActionListener(e -> HandlingOpen());

        JMenuItem SaveItem = CreateMenuItem("Save");
        SaveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        SaveItem.addActionListener(e -> HandlingSave());

        JMenuItem SaveASItem = CreateMenuItem("Save As");
        SaveASItem.addActionListener(e -> HandlingSaveAs());

        JMenuItem ExitItem = CreateMenuItem("Exit");
        ExitItem.addActionListener(e -> System.exit(0));

        FileMenu.add(NewItem);
        FileMenu.add(OpenItem);
        FileMenu.add(SaveItem);
        FileMenu.add(SaveASItem);
        FileMenu.addSeparator();
        FileMenu.add(ExitItem);

//        Menu (Edit)

        JMenu EditMenu = CreateMenu("Edit");

        JMenuItem UndoItem = CreateMenuItem("Undo");
        UndoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
        UndoItem.addActionListener(e -> HandlingUndo());

        JMenuItem RedoItem = CreateMenuItem("Redo");
        RedoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK));
        RedoItem.addActionListener(e -> HandlingRedo());

        JMenuItem FindItem = CreateMenuItem("Find & Replace");
        FindItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK));
        FindItem.addActionListener(e -> HandlingSearch());

        EditMenu.add(UndoItem);
        EditMenu.add(RedoItem);
        EditMenu.addSeparator();
        EditMenu.add(FindItem);

//        Menu (Run)

        JMenu RunMenu = CreateMenu("Run");
        JMenuItem RunItem = CreateMenuItem("Run");
        RunItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        RunItem.addActionListener(e -> HandlingRun());
        RunMenu.add(RunItem);

//        Adding Menu to MenuBar

        MenuBar.add(FileMenu);
        MenuBar.add(EditMenu);
        MenuBar.add(RunMenu);

        return MenuBar;
    }

//    ====== Action Handlers ======

//    For handling new file

    public void HandlingNew() {

        int CountUntitledTabs = 0;
        for (int i = 0; i < TabbedPane.getTabCount(); i++) {
            if (TabbedPane.getTitleAt(i).startsWith("Untitled")) {
                CountUntitledTabs++;
            }
        }

        String Title = "Untitled-" + (CountUntitledTabs + 1);

        AddNewTab(Title);
    }

//    For Opening a file

    public  void HandlingOpen() {
        FileManager.FileResult res = FileManager.OpenFile(frame);
        if (res != null) {
            AddNewTab(res.file.getName());
            EditorTab Tab = getCurrentTab();
            Tab.setText(res.Content);
            Tab.setCurrentFile(res.file);
            TabbedPane.setTitleAt(TabbedPane.getSelectedIndex(), res.file.getName());
            statusBar.UpdateFile(res.file.getName());
            frame.setTitle("AnyCode — " + res.file.getName());
        }
    }

//    For Saving a file

    public void HandlingSave() {
        EditorTab Tab = getCurrentTab();
        if (Tab == null) {
            return;
        }
        if (Tab.getCurrentFile() == null) {
            HandlingSaveAs();
        } else {
            FileManager.SaveFile(frame, Tab.getCurrentFile(), Tab.getText());
            frame.setTitle("AnyCode — " + Tab.getCurrentFile().getName());
        }
    }

//    For Saving As file

    public void HandlingSaveAs() {
        EditorTab Tab = getCurrentTab();
        if (Tab == null) {
            return;
        }
        File SavedFile = FileManager.SaveFileAs(frame, Tab.getText());
        if (SavedFile != null) {
            Tab.setCurrentFile(SavedFile);
            TabbedPane.setTitleAt(TabbedPane.getSelectedIndex(), SavedFile.getName());
            statusBar.UpdateFile(SavedFile.getName());
            frame.setTitle("AnyCode — " + SavedFile.getName());
        }
    }

//    For Running of code

    public void HandlingRun() {
        EditorTab Tab = getCurrentTab();
        if (Tab == null) return;

        // force save first
        if (Tab.getCurrentFile() == null) {
            HandlingSaveAs();
        } else {
            HandlingSave();
        }

        outputPanel.setVisible(true);
        frame.revalidate();
        codeExecuter.run(Tab.getCurrentFile(), outputPanel , Tab);
    }

//   For Undo

    public void HandlingUndo(){

        if (getCurrentTab() != null) getCurrentTab().Undo();
    }

//   For Redo

    public void HandlingRedo(){

        if (getCurrentTab() != null) getCurrentTab().Redo();
    }

//    For Search

    public void HandlingSearch(){
        EditorTab tab = getCurrentTab();
        if (tab != null) {
            new Find_Replace(frame, tab.getCodeEditor()).setVisible(true);
        }
    }

//    Helper Method for styled Menu

    private JMenu CreateMenu(String MenuName) {
        JMenu menu = new JMenu(MenuName);
        menu.setForeground(new Color(212, 212, 212));
        menu.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return menu;
    }

//  Helper Method for styled MenuItem

    private JMenuItem CreateMenuItem(String Name) {
        JMenuItem MenuItem = new JMenuItem(Name);
        MenuItem.setBackground(new Color(37, 37, 37));
        MenuItem.setForeground(new Color(212, 212, 212));
        MenuItem.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        MenuItem.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return MenuItem;
    }

//        Method for Opening the CodeEditor

    public void Open() {
        frame.setVisible(true);
    }
}