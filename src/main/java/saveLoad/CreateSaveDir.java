package saveLoad;

import constant.GameProperties;

import java.io.File;

public abstract class CreateSaveDir {
    private static void createSaveDirSec() {
        File saveDir = new File(GameProperties.SAVE_PATH);
        saveDir.mkdir();
    }

    public static void createSaveDir() {
        File saveDir = new File(GameProperties.SAVE_PATH);

        if(!saveDir.exists()) {
            createSaveDirSec();
        }
    }
}
