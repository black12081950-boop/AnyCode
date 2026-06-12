import java.io.*;

public class RURunner {

    private static final String RU_JAR_PATH = "\"D:\\UET IMP FILES\\ru.jar.zip\"";

    private static Process RunningProcess;
    private static PrintWriter ProcessInput;

    public static void run(File file, OutputPanel outputPanel, EditorTab currentTab) {

        StopPreviousProcess();

        String jarPath = RU_JAR_PATH;

        if (jarPath == null) {
            File sibling = new File(file.getParent(), "ru.jar");
            if (sibling.exists()) {
                jarPath = sibling.getAbsolutePath();
            } else {
                outputPanel.Print("ru.jar not found! Place ru.jar next to your .ru file, " +
                        "or set RU_JAR_PATH in RuRunner.java.");
                return;
            }
        }

        ProcessBuilder pb = new ProcessBuilder("java", "-jar", jarPath, file.getAbsolutePath());
        pb.redirectErrorStream(true);
        pb.directory(file.getParentFile());

        try {
            outputPanel.Clear();
            outputPanel.setVisible(true);

            RunningProcess = pb.start();
            ProcessInput = new PrintWriter(
                    new OutputStreamWriter(RunningProcess.getOutputStream()), true);

            Thread outputThread = new Thread(new OutputReader(RunningProcess, outputPanel, currentTab));
            outputThread.start();

        } catch (IOException e) {
            outputPanel.Print("Failed to run RU: " + e.getMessage());
        }
    }

    public static void sendInput(String input) {
        if (ProcessInput != null) {
            ProcessInput.println(input);
        }
    }

    private static void StopPreviousProcess() {
        if (RunningProcess != null && RunningProcess.isAlive()) {
            RunningProcess.destroy();
        }
    }
}