import org.fife.ui.rsyntaxtextarea.SquiggleUnderlineHighlightPainter;
import org.fife.ui.rsyntaxtextarea.*;
import org.fife.ui.rtextarea.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;


public class EditorTab {

    private RSyntaxTextArea CodeEditor ;
    private RTextScrollPane ScrollPane;
    private File CurrentFile;
    private String TabTitle;
    private StatusBar statusBar;

//    Constructor of EditorTab

    public EditorTab(String TabTitle , StatusBar statusBar){
        this.TabTitle = TabTitle;
        this.statusBar = statusBar;
        this.CurrentFile = null;
        CodeEditor = BuildCodeEditor();

//        Designing & Making layout of window using RTextScrollPane

        ScrollPane = new RTextScrollPane(CodeEditor);
        ScrollPane.setLineNumbersEnabled(true);
        ScrollPane.setPreferredSize(null);

        ScrollPane.setBackground(new Color(30, 30, 30));
        ScrollPane.getViewport().setBackground(new Color(30, 30, 30));
        ScrollPane.setViewportBorder(BorderFactory.createEmptyBorder()); // remove white border



//        Gutter (Left-Straight Line in-front of Line Numbers)

        Gutter gutter = ScrollPane.getGutter();
        gutter.setBackground(new Color(37,37,37));
        gutter.setLineNumberColor(new Color(133,133,133));
        gutter.setLineNumberFont(new Font("JetBrains Mono" , Font.PLAIN , 13));

    }

//        Method For Window (Code Editor)

    private RSyntaxTextArea BuildCodeEditor(){
        RSyntaxTextArea editor = new RSyntaxTextArea();

//        We are making this editor for JAVA Syntax

        editor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        editor.setCodeFoldingEnabled(true);
        editor.setAntiAliasingEnabled(true);
        editor.setFont(new Font("JetBrains Mono" , Font.PLAIN , 14));
        editor.setTabSize(4);       // Means 4-Spaces at single Tab

//        Dark-Theme Pictorial representation

        editor.setBackground(new Color(30,30,30));
        editor.setForeground(new Color(212,212,212));
        editor.setCaretColor(new Color(212,212,212));
        editor.setSelectionColor(new Color(38,79,120));
        editor.setCurrentLineHighlightColor(new Color(40,40,40));

//        Syntax Highlighting according to java

        SyntaxScheme scheme = editor.getSyntaxScheme();
        scheme.getStyle(Token.RESERVED_WORD).foreground = new Color(86,156,214);                // Color for Reserved Words
        scheme.getStyle(Token.LITERAL_STRING_DOUBLE_QUOTE).foreground = new Color(206,145,120); // Color for Strings
        scheme.getStyle(Token.COMMENT_EOL).foreground = new Color(106,153,85);                  // Color for Comments
        scheme.getStyle(Token.DATA_TYPE).foreground = new Color(78,201,176);                    // Color for Data Type
        scheme.getStyle(Token.LITERAL_NUMBER_DECIMAL_INT).foreground = new Color(181,206,168);  // Color for Numbers
        editor.revalidate();

        editor.addCaretListener(e -> {
            try {
                int position = editor.getCaretPosition();
                int Line = editor.getLineOfOffset(position) + 1;      // +1 because starts at 0
                int Column = position - editor.getLineStartOffset(Line - 1) + 1; // +1 same reason
                if (statusBar != null) {
                    statusBar.UpdateLineCol(Line, Column);
                }
            } catch (Exception ex) {}
        }

        );

        return editor;
    }

    // Clears all previous red highlights

    public void ClearErrorHighlights() {
        CodeEditor.getHighlighter().removeAllHighlights();
    }

    // This highlights a specific line red

    public void highlightErrorLine(int lineNumber) {
            try {

                int start = CodeEditor.getLineStartOffset(lineNumber - 1);
                int end   = CodeEditor.getLineEndOffset(lineNumber - 1);


                SquiggleUnderlineHighlightPainter painter =
                        new SquiggleUnderlineHighlightPainter(new Color(255, 50, 50));

                CodeEditor.getHighlighter().addHighlight(start, end, painter);

            }
            catch (Exception e) {}

    }


//    Getters

    public RSyntaxTextArea getCodeEditor() {
        return CodeEditor;                    // Returns Code editor so we can write/read it's text
    }
    public RTextScrollPane getScrollPane() {
        return ScrollPane;                    // Returns Scrollpane to add it to the tab we are working on
    }
    public File getCurrentFile() {
        return CurrentFile;                   // Returns Current file (opened file) it can be 'null'
    }
    public String getTabTitle() {
        return TabTitle;
    }
    public String getText(){
        return CodeEditor.getText();  // Returns all the text in the Code editor
    }

//    Setters

    public void setText(String Text){
        CodeEditor.setText(Text);               // Sets text into the editor
        CodeEditor.setCaretPosition(0);         // Moves Cursor/Caret to the top
    }

    public void setCurrentFile(File CurrentFile) {
        this.CurrentFile = CurrentFile;
        this.TabTitle = CurrentFile.getName();  // Update Tab Title to file name

//        if (CurrentFile.getName().endsWith(".ru")) {
//            CodeEditor.setSyntaxEditingStyle("text/ru");
//        } else {
//            CodeEditor.setSyntaxEditingStyle(org.fife.ui.rsyntaxtextarea.SyntaxConstants.SYNTAX_STYLE_JAVA);
//        }
        SetSyntaxStyle(CurrentFile.getName());
    }

//    Syntax Style of RU

    private void SetSyntaxStyle(String fileName) {
        if (fileName.endsWith(".ru")) {
            AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory)
                    TokenMakerFactory.getDefaultInstance();
            atmf.putMapping("text/ru", "RUTokenMaker");
            CodeEditor.setSyntaxEditingStyle("text/ru");
        }
        else {
            CodeEditor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        }
    }

    public void Undo(){
        CodeEditor.undoLastAction();            // Undo the last action performed
    }

    public void Redo(){
        CodeEditor.redoLastAction();            // Redo the last Undo performed
    }
}