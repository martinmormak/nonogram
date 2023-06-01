package sk.tuke.gamestudio;

import sk.tuke.gamestudio.core.Field;
import sk.tuke.gamestudio.entity.Data;
import sk.tuke.gamestudio.service.DataService;
import sk.tuke.gamestudio.service.DataServiceJDBC;
import sk.tuke.gamestudio.service.FieldGeneratorTXT;

import java.io.*;

public class UploadDataFromFileToDatabase {
    private DataService dataService=new DataServiceJDBC();
    private Field field;
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        FieldGeneratorTXT fieldGeneratorTXT=new FieldGeneratorTXT();
        File directory = new File("."); // Replace with the path to your directory
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".txt"));
        // Only include files that end with ".txt"
        int i=1001;//maybe higher number will be better
        UploadDataFromFileToDatabase uploadDataFromFileToDatabase=new UploadDataFromFileToDatabase();
        for (File file : files) {
            uploadDataFromFileToDatabase.loadGame(fieldGeneratorTXT,file);
            try {
                System.out.println(i);
                uploadDataFromFileToDatabase.saveGame(i);
                i++;
            }catch (Exception e){
                System.out.println(e.toString());
            }
        }
    }

    private void loadGame(FieldGeneratorTXT fieldGeneratorTXT,File file) throws FileNotFoundException {
        this.field=fieldGeneratorTXT.generate(file.getName());
    }

    public void saveGame(int slotNumber){
        try {
            dataService.saveGameData(preparedSavedGame(slotNumber),slotNumber);
        }catch (Exception e){
            //throw new RuntimeException("Couldn't uploaded game to database");
            throw new RuntimeException(e);
        }
    }

    private Data preparedSavedGame(int slotNumber){
        File dataFile=new File("gameToSave.bin");
        try(FileOutputStream fileOutputStream=new FileOutputStream(dataFile);
            ObjectOutputStream objectOutputStream=new ObjectOutputStream(fileOutputStream)){
            objectOutputStream.writeObject(field);
            objectOutputStream.flush();
            try(FileInputStream fileInputStream=new FileInputStream(dataFile)) {
                return new Data(slotNumber,fileInputStream.readAllBytes(),"savedtemplate",0,null,"nonogram");
            }
        } catch (IOException e) {
            //throw new RuntimeException("Couldn't prepare board template to be uploaded to database");
            throw new RuntimeException(e);
        }
    }
}
