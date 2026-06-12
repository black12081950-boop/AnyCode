import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CodeShot {

    public static void Take(RSyntaxTextArea editor, String fileName, JFrame frame) {

        int padding     = 40;  // space around the code
        int titleHeight = 40; // height of the title bar
        int editorW     = editor.getWidth();
        int editorH     = editor.getHeight();
        int totalW      = editorW + (padding * 2);
        int totalH      = editorH + (padding * 2) + titleHeight;


        BufferedImage image = new BufferedImage(
                totalW, totalH, BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D graphics = image.createGraphics();


        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);


        GradientPaint gradient = new GradientPaint(
                0, 0, new Color(20, 20, 40),       // dark blue top
                0, totalH, new Color(10, 10, 20)   // darker blue bottom
        );
        graphics.setPaint(gradient);
        graphics.fillRect(0, 0, totalW, totalH);


        RoundRectangle2D roundedWindow = new RoundRectangle2D.Float(
                padding / 2, padding / 2,
                totalW - padding, totalH - padding,
                20, 20  // corner radius
        );
        graphics.setColor(new Color(30, 30, 30));
        graphics.fill(roundedWindow);


        graphics.setColor(new Color(45, 45, 45));
        graphics.fillRoundRect(padding / 2, padding / 2,
                totalW - padding, titleHeight, 20, 20);


        int dotY   = padding / 2 + titleHeight / 2;
        int dotX   = padding / 2 + 20;
        int dotSize = 12;

        graphics.setColor(new Color(255, 95, 87));   // red dot
        graphics.fillOval(dotX, dotY - dotSize/2, dotSize, dotSize);

        graphics.setColor(new Color(255, 189, 46));  // yellow dot
        graphics.fillOval(dotX + 20, dotY - dotSize/2, dotSize, dotSize);

        graphics.setColor(new Color(39, 201, 63));   // green dot
        graphics.fillOval(dotX + 40, dotY - dotSize/2, dotSize, dotSize);


        graphics.setColor(new Color(180, 180, 180));
        graphics.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        FontMetrics fm = graphics.getFontMetrics();
        int textX = (totalW - fm.stringWidth(fileName)) / 2;
        int textY = padding / 2 + titleHeight / 2 + fm.getAscent() / 2;
        graphics.drawString(fileName, textX, textY);


        Graphics2D editorGraphics = (Graphics2D) graphics.create(padding, padding + titleHeight, editorW, editorH);
        editor.paint(editorGraphics);
        editorGraphics.dispose();

        graphics.dispose();


        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save Snapshot");
        chooser.setSelectedFile(new File(fileName.replace(".java", "") + "_snapshot.png"));

        int result = chooser.showSaveDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File outputFile = chooser.getSelectedFile();
            try {
                ImageIO.write(image, "PNG", outputFile);
                JOptionPane.showMessageDialog(frame,
                        "Snapshot saved! 📸",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame,
                        "Failed to save: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}