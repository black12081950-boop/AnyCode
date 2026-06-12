import javax.swing.*;
import java.io.*;
import java.util.regex.*;

public class OutputReader implements Runnable {
    private Process process;
    private OutputPanel outputPanel;
    private EditorTab CurrentTab;

//    Constructor of OutputReader

    public OutputReader(Process process , OutputPanel outputpanel ,EditorTab CurrentTab){
        this.process = process;
        this.outputPanel = outputpanel;
        this.CurrentTab = CurrentTab;
    }

    @Override
    public void run(){
        try{
//            This clear previous highlights before new run

            SwingUtilities.invokeLater(() -> CurrentTab.ClearErrorHighlights());

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

//            For Java

            Pattern errorPattern = Pattern.compile(".*:(\\d+): error:.*");

//            For RU

            Pattern ruError = Pattern.compile("\\[Lakeer (\\d+)\\].*");

            String Line;

//            Read the code until the there is no line left
            while((Line = reader.readLine()) != null){
                final String FinalLine = Line;

//              Matching if the line matches our pattern

                Matcher matcher = errorPattern.matcher(FinalLine);
                if (matcher.matches()) {

                    // For extracting the line number
                    int lineNum = Integer.parseInt(matcher.group(1));

                    // For highlighting it red in the editor
                    SwingUtilities.invokeLater(() ->
                            CurrentTab.highlightErrorLine(lineNum));
                }

//                  Check .ru error

                Matcher ruMatcher = ruError.matcher(FinalLine);
                if (ruMatcher.matches()) {
                    int lineNum = Integer.parseInt(ruMatcher.group(1));
                    SwingUtilities.invokeLater(() -> CurrentTab.highlightErrorLine(lineNum));
                }

                SwingUtilities.invokeLater(() -> outputPanel.Print(FinalLine));
            }
            SwingUtilities.invokeLater(() -> outputPanel.Print("\nProgram Finished with exit code 0"));
        }
        catch (IOException e){
            SwingUtilities.invokeLater(() ->
                    outputPanel.Print("Error: " + e.getMessage()));
        }
    }
}
