package saveLoad;

import constant.GameProperties;
import gameObjects.Game;
import java.io.*;

/**
 * The SerializationUtils class provide utility methods for objects serialization and deserialization
 */
public abstract class SerializationUtils {
    /**
     * The constructor is useless for this class
     */
    private SerializationUtils(){}

    /**
     * serializes a Game object and writes it to a file.
     * @param game The Game object to be serialized.
     * @param filename The name of the file to write the serialized object to.
     */
    public static void serialisationGame(Game game, String filename){
        // Create the save directory if it doesn't exist
        CreateSaveDir.createSaveDir();

        try (FileOutputStream fileOut = new FileOutputStream(GameProperties.SAVE_PATH + filename);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)){

            // Serialize the Game object and write it to the file output stream
            out.writeObject(game);
            System.out.println("game serialized : "+ filename);

        } catch (IOException e){
            // Handle any input/output exceptions that occur during serialization
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
        try (FileInputStream fileIn = new FileInputStream(GameProperties.SAVE_PATH + filename);
        ObjectInputStream in = new ObjectInputStream(fileIn)){

            // Deserialize the input data and convert it to a Game object
            game = (Game) in.readObject();
            System.out.println("game deserialized : "+ filename);
            return game;

        } catch (IOException | ClassNotFoundException e){
            // Handle any input/output exceptions or class not found exception that occur during deserialization
            e.printStackTrace();
        }
        return null;
    }

}


