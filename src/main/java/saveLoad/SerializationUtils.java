package saveLoad;

import constant.GameProperties;
import gameObjects.Game;
import java.io.*;

/**
 * The SerializationUtils class provide utility methods for objects serialization and deserialization
 */
public class SerializationUtils {
    /**
     * serializes a Game object and writes it to a file.
     * @param game The Game object to be serialized.
     * @param filename The name of the file to write the serialized object to.
     */
    public static void serialisationGame(Game game, String filename){
        // open output stream of data which write in file filename
        // ObjectOutputStream responsible for serialization of objects
        // and writing of serialized data in output stream of fileOutputStream
        CreateSaveDir.createSaveDir();
        try (FileOutputStream fileOut = new FileOutputStream(GameProperties.SAVE_PATH + filename);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)){
            // take Game and convert it in serialized representation
            // write this representation in the file output stream
            out.writeObject(game);
            System.out.println("partie serializée : "+ filename);
        // In and Out exception
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    /**
     * Deserializes a Game object from a file.
     * @param filename The name of the file to deserialize the Game object from.
     * @return The deserialized Game object, or null if and error occurs during deserialization.
     */
    public static Game deserialisationGame(String filename){
        Game game = null;
        // open input stream which will read filename
        // ObjectInputStream responsible for deserialization of the object
        // using data input stream from FileInputStream
        try (FileInputStream fileIn = new FileInputStream(GameProperties.SAVE_PATH + filename);
        ObjectInputStream in = new ObjectInputStream(fileIn)){
            //deserialization of input data, convert it in Game object
            game = (Game) in.readObject();
            System.out.println("partie deserializée : "+ filename);
            return game;
        // In and Out error or class not found, null is returned
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }

}


