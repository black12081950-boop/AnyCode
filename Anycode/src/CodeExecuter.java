import java.io.*;

public class CodeExecuter{
    private Process RunningProcess;
    private PrintWriter ProcessInput;

    public void run(File file , OutputPanel outputPanel , EditorTab CurrentTab){


        if (file.getName().endsWith(".ru")) {
            RURunner.run(file, outputPanel, CurrentTab);
            return;
        }

        StopPreviousProcess();

        String FolderPath = file.getParent();
        String FileName = file.getName().replace(".java" , "");

        // For checking if the file has a 'package'

        String PackageName = GetPackageName(file);
        String RunCommand;

//      If it has package then execute from parent folder and run with the package

        if (PackageName != null) {
            FolderPath = file.getParentFile().getParent();
            RunCommand = "javac \"" + PackageName + "\\*.java\" && " + "java " + PackageName + "." + FileName;
        }

//      If it has no package then run normally

        else {

            RunCommand = "javac *.java && " + "java " + FileName;
        }

        ProcessBuilder P = new ProcessBuilder(
                "cmd.exe", "/c", "cd /d \"" + FolderPath + "\" && " + RunCommand
        );

        P.redirectErrorStream(true);

        if (PackageName != null) {
            P.directory(new File(FolderPath));
        }
        else {
            P.directory(file.getParentFile());
        }

        try {

            outputPanel.Clear();

            outputPanel.setVisible(true);

            RunningProcess = P.start();

            ProcessInput = new PrintWriter(new OutputStreamWriter(RunningProcess.getOutputStream()),true);

            // Creating thread separately

            Thread outputThread = new Thread(new OutputReader(RunningProcess, outputPanel , CurrentTab));

            // Start the thread we created seperately

            outputThread.start();

        } catch (IOException e) {

            outputPanel.Print("Failed to run: " + e.getMessage());
        }
    }

    public void sendInput(String input) {

        if (ProcessInput != null) {
            
            ProcessInput.println(input);
        }
        RURunner.sendInput(input);
    }

    private void StopPreviousProcess() {

        if (RunningProcess != null && RunningProcess.isAlive())
        {
            RunningProcess.destroy();
        }
    }

//  Helper method for Package support

    private String GetPackageName(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String firstLine = reader.readLine();       // It reads first line only
            reader.close();

            if (firstLine != null && firstLine.startsWith("package")) {

//                 Extract name between "package " and ";"

                return firstLine.replace("package", "").replace(";", "").trim();
            }
        } catch (IOException e) {}

        return null;        // In case no package found
    }
}