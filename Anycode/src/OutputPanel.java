import javax.swing.*;
import java.awt.*;
public class OutputPanel extends JPanel {
    private JTextArea OutputArea;
    private JTextField InputField;
    private JButton SenderButton;

//  Constructor of OutputPanel

    public OutputPanel(){
        setLayout(new BorderLayout());
        setBackground(new Color(30,30,30));
        setPreferredSize(new Dimension(0,200));

//        Top Bar

        JPanel TopBar = new JPanel(new BorderLayout());
        TopBar.setBackground(new Color(37, 37, 37));
        TopBar.setPreferredSize(new Dimension(0, 25));

        JLabel Label = new JLabel("  Output");
        Label.setForeground(new Color(150, 150, 150));
        Label.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JButton CloseButton = new JButton("×");
        CloseButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        CloseButton.setFocusPainted(false);
        CloseButton.setContentAreaFilled(false);
        CloseButton.setBorder(BorderFactory.createEmptyBorder(0, 4, 4, 4));
        CloseButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        CloseButton.addActionListener(e -> setVisible(false));

        TopBar.add(Label, BorderLayout.WEST);
        TopBar.add(CloseButton, BorderLayout.EAST);

        add(TopBar, BorderLayout.NORTH);

//        Output Area (Where output will be shown)

        OutputArea = new JTextArea();
        OutputArea.setBackground(new Color(30,30,30));
        OutputArea.setForeground(new Color(212,212,212));
        OutputArea.setFont(new Font("JetBrains Mono" , Font.PLAIN , 13));
        OutputArea.setCaretColor(new Color(212,212,212));
        OutputArea.setEditable(false);

//        Scroll-Pane for Long Output

        JScrollPane ScrolPane = new JScrollPane(OutputArea);
        ScrolPane.setBackground(new Color(30,30,30));
        ScrolPane.setBorder(BorderFactory.createEmptyBorder());
        ScrolPane.getViewport().setBackground(new Color(30,30,30));
        add(ScrolPane , BorderLayout.CENTER);

//      Input row at the bottom of frame

        JPanel InputRow = new JPanel(new BorderLayout());
        InputRow.setBackground(new Color(37,37,37));

//      Input Field

        InputField = new JTextField();
        InputField.setBackground(new Color(37,37,37));
        InputField.setForeground(new Color(212,212,212));
        InputField.setFont(new Font("JetBrains Mono" , Font.PLAIN , 13));
        InputField.setCaretColor(new Color(212,212,212));
        InputField.setBorder(BorderFactory.createEmptyBorder(4,8,4,8));

//      Input Sender Button

        SenderButton = new JButton("Input");
        SenderButton.setBackground(new Color(0,120,0));
        SenderButton.setForeground(new Color(255,255,255));
        SenderButton.setFont(new Font("Segoe UI" , Font.PLAIN , 12));
        SenderButton.setFocusPainted(false);
        SenderButton.setBorder(BorderFactory.createEmptyBorder(4,12,4,12));

//        Adding into the Output-Panel

        InputRow.add(InputField , BorderLayout.CENTER);
        InputRow.add(SenderButton , BorderLayout.EAST);
        add(InputRow , BorderLayout.SOUTH);
    }

//  Print a new line of Text in output area

    public void Print(String Text){
        OutputArea.append(Text + "\n");
//        for auto-scroll to the end we will use
        OutputArea.setCaretPosition(OutputArea.getDocument().getLength());
    }

//    Clears the OutputArea before new Running of a file/code

    public void Clear(){
        OutputArea.setText("");
    }

//    Getters

    public JButton getSenderButton() {
        return SenderButton;
    }

    public JTextField getInputField() {
        return InputField;
    }

    public JTextArea getOutputArea(){
        return OutputArea;
    }

}