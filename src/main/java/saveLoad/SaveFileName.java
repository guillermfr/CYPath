package saveLoad;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This class is useful to manage the name of a save file.
 */
public class SaveFileName {

    /**
     * Name of the save file.
     */
    StringProperty saveName;

    /**
     * Constructor of the class.
     * The only parameter it takes is the name of the save file, and puts it in the corresponding attribute.
     * @param saveName Name of the save file.
     */
    public SaveFileName(String saveName) {
        this.saveName = new SimpleStringProperty(saveName);
    }

    /**
     * Getter for the saveName attribute.
     * @return the save name as a StringProperty variable.
     */
    public StringProperty saveNameProperty() {
        return saveName;
    }

    /**
     * Another getter for the saveName attribute.
     * This one returns the attribute as a String instead of a StringProperty.
     * @return the save name as a String variable.
     */
    public String getSaveName() {
        return saveName.get();
    }

    /**
     * Setter for the saveName attribute.
     * @param saveName Name of the save file.
     */
    public void setSaveName(String saveName) {
        this.saveName.set(saveName);
    }
}
