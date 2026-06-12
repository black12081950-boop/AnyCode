import org.fife.ui.rtextarea.*;
import org.fife.ui.rsyntaxtextarea.*;
import javax.swing.*;
import java.awt.*;

public class Find_Replace extends JDialog {

    private JTextField FindField;
    private JTextField ReplaceField;
    private RSyntaxTextArea editor;

    public Find_Replace(JFrame parent, RSyntaxTextArea editor) {
        super(parent, "Find & Replace", false);
        this.editor = editor;

        setSize(400, 180);
        setLocationRelativeTo(parent);
        setResizable(false);
        setLayout(new BorderLayout());

//        Input Fields for searching

        JPanel fieldsPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 8, 12));

        FindField = new JTextField();
        ReplaceField = new JTextField();

        fieldsPanel.add(new JLabel("Find:"));
        fieldsPanel.add(FindField);
        fieldsPanel.add(new JLabel("Replace:"));
        fieldsPanel.add(ReplaceField);

//      Buttons for Finding and Replacing

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));

        JButton FindButton       = new JButton("Find");
        JButton ReplaceButton    = new JButton("Replace");
        JButton ReplaceAllButton = new JButton("Replace All");
        JButton CloseButton      = new JButton("Close");

//      FIND button

        FindButton.addActionListener(e -> {
            SearchContext context = new SearchContext();
            context.setSearchFor(FindField.getText());
            context.setSearchForward(true);
            context.setMarkAll(true);

            editor.setMarkAllHighlightColor(new Color(255, 210, 50, 80));

            boolean found = SearchEngine.find(editor, context).wasFound();

            if (!found) {
                // start from beginning and try again
                editor.setCaretPosition(0);
                found = SearchEngine.find(editor, context).wasFound();
                if (!found) {
                    JOptionPane.showMessageDialog(this, "Text not found!");
                }
            }
        });

//      REPLACE button

        ReplaceButton.addActionListener(e -> {
            SearchContext context = new SearchContext();
            context.setSearchFor(FindField.getText());
            context.setReplaceWith(ReplaceField.getText());
            SearchEngine.replace(editor, context);
        });

//      REPLACE ALL button

        ReplaceAllButton.addActionListener(e -> {
            SearchContext context = new SearchContext();
            context.setSearchFor(FindField.getText());
            context.setReplaceWith(ReplaceField.getText());
            int count = SearchEngine.replaceAll(editor, context).getCount();
            JOptionPane.showMessageDialog(this, count + " replacements made!");
        });

//        Close Button

        CloseButton.addActionListener(e -> dispose());

        buttonPanel.add(FindButton);
        buttonPanel.add(ReplaceButton);
        buttonPanel.add(ReplaceAllButton);
        buttonPanel.add(CloseButton);

        add(fieldsPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}