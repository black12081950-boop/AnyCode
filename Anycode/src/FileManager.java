import javax.swing.*;
import java.io.*;
import java.nio.file.*;

public class FileManager {

//    Method for Opening File (Open)

    public static FileResult OpenFile(JFrame ParentFrame){
        JFileChooser FileChoser = new JFileChooser();
        FileChoser.setDialogTitle("Open File");

        int res = FileChoser.showOpenDialog(ParentFrame);

        if(res == JFileChooser.APPROVE_OPTION){

            File file = FileChoser.getSelectedFile();
            try{
                String Content = Files.readString(file.toPath());
                return new FileResult(file , Content);
            }
            catch (IOException er){
                JOptionPane.showMessageDialog(ParentFrame, "Could not open file: " + er.getMessage() , "Error" , JOptionPane.ERROR_MESSAGE);
            }
        }
        return null;
    }

//    Method for Saving File (Save)

    public static boolean SaveFile(JFrame ParentFrame  , File file , String Content){
        try{
            Files.writeString(file.toPath() , Content);
            return true;
        }
        catch (IOException er){
            JOptionPane.showMessageDialog(ParentFrame , "Could not save file: " + er.getMessage(), "Error" , JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

//    Method for Saving File As (Save As)

    public static File SaveFileAs(JFrame ParentFrame , String Content){
        JFileChooser FileChooser = new JFileChooser();
        FileChooser.setDialogTitle("Save File As");

        int res = FileChooser.showOpenDialog(ParentFrame);

        if(res == JFileChooser.APPROVE_OPTION){
            File file = FileChooser.getSelectedFile();
            if(!file.getName().contains(".")){
                file = new File(file.getAbsolutePath() + ".java");
            }
            boolean success = SaveFile(ParentFrame , file , Content);
            if(success){
                JOptionPane.showMessageDialog(ParentFrame, "File saved: " + file.getAbsolutePath());
                return file;
            }
        }
        return null;
    }

//    Method for Getting File and it's content together

    public static class FileResult {
        public File file;
        public String Content;

        public FileResult(File file, String Content) {
            this.file = file;
            this.Content = Content;
        }
    }
}