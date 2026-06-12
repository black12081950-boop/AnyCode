import javax.swing.*;
import java.awt.*;
import java.util.*;
import javax.swing.Timer;

public class StatusBar extends JPanel {

    private JLabel LineColoumnLabel;  // shows line and column number
    private JLabel FileLabel;     // shows current file name
    private JLabel QuoteLabel;
    private JLabel TimerLabel;
    private String[] Quotes = {
            "☕ Code never lies, comments sometimes do!",
            "🔥 First, solve the problem. Then, write the code!",
            "💪 The best error message is the one that never shows up!",
            "😄 It works on my machine!",
            "🚀 Talk is cheap. Show me the code!",
            "⚡ Any fool can write code that a computer can understand!",
            "🏆 Code is like humor. When you have to explain it, it's bad!",
            "😎 Fix the cause, not the symptom!",
            "🌙 One more feature and then I'll sleep!",
            "💡 Simplicity is the soul of efficiency!",
            "🎯 Make it work, make it right, make it fast!",
            "😂 I don't always test my code, but when I do, I do it in production!",
            "🐛 A bug is never just a mistake, it represents something bigger!",
            "🧠 Programming is thinking, not typing!",
            "💻 The computer was born to solve problems that did not exist before!",
            "😂 There are two ways to write error free programs, only the third one works!",
            "🔥 Without requirements and design, programming is the art of adding bugs!",
            "📚 Experience is the name everyone gives to their mistakes!",
            "🎯 The best thing about a boolean is even if you are wrong you are only off by a bit!",
            "🚗 Java is to JavaScript what car is to carpet!",
            "🔍 Debugging is like being the detective in a crime movie where you are also the murderer!",
            "🙏 Software and cathedrals are much the same, first we build them then we pray!",
            "✨ It is not a bug, it is an undocumented feature!",
            "💡 The function of good software is to make the complex appear simple!",
            "😎 Always code as if the guy maintaining your code is a violent psychopath!",
            "☕ Programmer: A machine that turns coffee into code!",
            "🤦 My code works, I have no idea why!",
            "🚢 It works on my machine, ship my machine!",
            "😭 Dear keyboard, sorry for all the key smashing!",
            "🏆 First position is not a dream, it is a plan!",
            "💪 You are not a beginner anymore, look how far you have come!",
            "🚀 Keep going, your future self will thank you!"
    };

    public StatusBar() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));

//      Line and Column Labeling

        LineColoumnLabel = new JLabel("Line: 1  Col: 1");
        LineColoumnLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

//        File name Labeling

        FileLabel = new JLabel("No File");
        FileLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        FileLabel.setHorizontalAlignment(SwingConstants.CENTER);

//        Quote Labeling

        QuoteLabel = new JLabel();
        QuoteLabel.setFont(new Font("Segoe UI Emoji", Font.ITALIC, 12));
        QuoteLabel.setForeground(new Color(200, 200, 200));
        QuoteLabel.setHorizontalAlignment(SwingConstants.RIGHT);

//      Method that instantly shows first quote

        UpdateQuote();

//      30 sec timer for updating next Quotes

        Timer QuoteTimer = new Timer(30000 ,e-> UpdateQuote());
        QuoteTimer.start();


        add(LineColoumnLabel, BorderLayout.WEST);
        add(FileLabel, BorderLayout.CENTER);

//      Time and Quote Labeling

        JPanel RightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        RightPanel.setOpaque(false);
        TimerLabel = new JLabel("🍅 25:00  ");
        TimerLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        RightPanel.add(TimerLabel);
        RightPanel.add(QuoteLabel);
        add(RightPanel, BorderLayout.EAST);
    }

    private void UpdateQuote() {
        // pick a random quote from the list
        int index = new Random().nextInt(Quotes.length);
        QuoteLabel.setText(Quotes[index] + "  ");
    }

    public void UpdateLineCol(int line, int col) {
        LineColoumnLabel.setText("Line: " + line + "  Col: " + col);
    }

    public void UpdateFile(String fileName) {
        FileLabel.setText(fileName);
    }

    public void UpdateTimer(String text) {
        TimerLabel.setText(text + "  ");
    }
}