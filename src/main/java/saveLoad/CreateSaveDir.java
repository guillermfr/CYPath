package saveLoad;

import constant.GameProperties;

import java.io.File;

/**
 * Utility class for creating the save directory.
 */
public abstract class CreateSaveDir {
    /**
     * Default constructor of the CreateSaveDir class.
     */
    public CreateSaveDir() {}
    /**
     * Creates the save directory.
     * This method is called internally to create the save directory if it doesn't exist.
     */
    private static void createSaveDirSec() {
        File saveDir = new File(GameProperties.SAVE_PATH);
        saveDir.mkdir();
    }

    /**
     * Creates the save directory if it doesn't exist.
     * If the save directory already exists, this method does nothing.
     */
    public static void createSaveDir() {
        File saveDir = new File(GameProperties.SAVE_PATH);

        if(!saveDir.exists()) {
            createSaveDirSec();
        }
    }
}
