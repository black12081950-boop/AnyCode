import javax.swing.*;
import java.awt.*;

public class FocusTimer {


    private static final int WORK_TIME  = 1 * 30; // 15 minutes in seconds
    private static final int BREAK_TIME = 5  * 60; // 5 minutes in seconds


    private Timer timer;
    private int secondsLeft;    // how many seconds remaining
    private boolean isWorking;  // true = work session, false = break session
    private boolean isRunning;  // true = timer is running

    private StatusBar statusBar; // to update the label
    private JFrame frame;        // to show popups


    public FocusTimer(StatusBar statusBar, JFrame frame) {
        this.statusBar = statusBar;
        this.frame = frame;
        this.secondsLeft = WORK_TIME;
        this.isWorking = true;
        this.isRunning = false;

        timer = new Timer(1000, e -> tick());
    }


    private void tick() {
        secondsLeft--;
        UpdateDisplay();

        if (secondsLeft <= 0) {
            if (isWorking) {
                isWorking = false;
                secondsLeft = BREAK_TIME;
                JOptionPane.showMessageDialog(frame,
                        "Great work! Take a 5 minute break! ☕",
                        "Break Time!", JOptionPane.INFORMATION_MESSAGE);
            }
            else {
                isWorking = true;
                secondsLeft = WORK_TIME;
                JOptionPane.showMessageDialog(frame,
                        "Break over! Back to coding! 🍅",
                        "Work Time!", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }


    private void UpdateDisplay() {
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft % 60;

        // Format like 25:00, 04:59 etc

        String time = String.format("%02d:%02d", minutes, seconds);
        String emoji = isWorking ? "🍅" : "☕";

        statusBar.UpdateTimer(emoji + " " + time);
    }


    public void Start() {
        if (!isRunning) {
            isRunning = true;
            timer.start();
        }
    }

    public void Pause() {
        if (isRunning) {
            isRunning = false;
            timer.stop();
        }
    }

    public void Stop() {
        timer.stop();
        isRunning = false;
        isWorking = true;
        secondsLeft = WORK_TIME;
        statusBar.UpdateTimer("🍅 25:00"); // reset display
    }

    public boolean IsRunning() {
        return isRunning;
    }
}