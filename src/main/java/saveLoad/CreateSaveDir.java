package saveLoad;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

public abstract class CreateSaveDir {
    public static void createSaveDir() {
        File saveDir = new File("target/classes/save");
        saveDir.mkdir();
    }

    public static void createSaveDirWithTest() {
        // We get the path of the save directory
        URL saveURL = CreateSaveDir.class.getResource("/save");

        File saveDir = null;
        try {
            if(saveURL != null) {
                saveDir = new File(saveURL.toURI());
            }
        } catch (URISyntaxException e) {
            System.out.println(e);
        }

        if(saveDir == null) {
            createSaveDir();
        }
    }
}
