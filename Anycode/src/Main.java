import com.formdev.flatlaf.FlatDarkLaf;
import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;

public class Main {
    public static void main(String[] args) {

//        For UI Setup

        FlatDarkLaf.setup();

//        For RU Language

        AbstractTokenMakerFactory TokenMaker = (AbstractTokenMakerFactory)
                TokenMakerFactory.getDefaultInstance();
        TokenMaker.putMapping("text/ru", "RUTokenMaker");

//        For AnyCode's Window

        javax.swing.SwingUtilities.invokeLater(() -> {});
        MainWindow window = new MainWindow();
        window.Open();
    }
}