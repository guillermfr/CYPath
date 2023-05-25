package saveLoad;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SaveFileName {

    StringProperty saveName;

    public SaveFileName(String saveName) {
        this.saveName = new SimpleStringProperty(saveName);
    }

    public StringProperty saveNameProperty() {
        return saveName;
    }

    public String getSaveName() {
        return saveName.get();
    }

    public void setSaveName(String saveName) {
        this.saveName.set(saveName);
    }
}
