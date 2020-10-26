package be.filip.intellij.settings;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializer;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Supports storing the application settings in a persistent way.
 * The {@link State} and {@link Storage} annotations define the name of the data and the file name where
 * these persistent application settings are stored.
 */
@State(
        name = "be.filip.intellij.settings.PluginSettings",
        storages = {@Storage("GitPrefixSettingsPlugin.xml")}
)
public class PluginSettings implements PersistentStateComponent<PluginSettings> {
//public class PluginSettings {

    private static PluginSettings instance;
    private static final String SETTING_COMMIT_MESSAGE_DELIMITER = "git.auto.prefix.delimiter";
    private static final String SETTING_USER_INITIAL_PREFIX = "NO NAME";

    private String commitMessageDelimiter = ": ";
    private String userInitials;
    private boolean commitOrderTicketuserInitials = true;

    private PluginSettings(){}

    public static PluginSettings getInstance(){
//        if (instance == null){
//            instance = new PluginSettings();
//
//            //Set unset Properties
//            instance.savePropertyIfUnset(SETTING_COMMIT_MESSAGE_DELIMITER, ": ");
//            instance.savePropertyIfUnset(SETTING_USER_INITIAL_PREFIX, "No Name: ");
//
//            //Load all properties
//            instance.load();
//        }
//
//        return instance;

        return ServiceManager.getService(PluginSettings.class);
    }

    /*
        public static PluginSettingsState getInstance() {
        return ServiceManager.getService(PluginSettingsState.class);
    }
     */

    /**
     * @return a component state. All properties, public and annotated fields are serialized. Only values, which differ
     * from the default (i.e., the value of newly instantiated class) are serialized. {@code null} value indicates
     * that the returned state won't be stored, as a result previously stored state will be used.
     * @see XmlSerializer
     */
    @Override
    public @Nullable PluginSettings getState() {
        return this;
    }

    /**
     * This method is called when new component state is loaded. The method can and will be called several times, if
     * config files were externally changed while IDE was running.
     * <p>
     * State object should be used directly, defensive copying is not required.
     *
     * @param state loaded component state
     * @see XmlSerializerUtil#copyBean(Object, Object)
     */
    @Override
    public void loadState(@NotNull PluginSettings state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    private void savePropertyIfUnset(String propertyName, String defaultValue){
        boolean valueSet = PropertiesComponent.getInstance().isValueSet(propertyName);
        if (!valueSet){
            PropertiesComponent.getInstance().setValue(propertyName, defaultValue);
        }
    }

    private void load(){
        this.commitMessageDelimiter = PropertiesComponent.getInstance().getValue(SETTING_COMMIT_MESSAGE_DELIMITER);
    }

    public void save(){
        PropertiesComponent.getInstance().setValue(SETTING_COMMIT_MESSAGE_DELIMITER, this.commitMessageDelimiter);
    }

    public String getCommitMessageDelimiter() {
        return commitMessageDelimiter;
    }

    public void setCommitMessageDelimiter(String commitMessageDelimiter) {
        this.commitMessageDelimiter = commitMessageDelimiter;
    }

    public String getUserInitials() {
        return userInitials;
    }

    public void setUserInitials(String userInitials) {
        this.userInitials = userInitials;
    }

    public boolean isCommitOrderTicketuserInitials() {
        return commitOrderTicketuserInitials;
    }

    public void setCommitOrderTicketuserInitials(boolean commitOrderTicketuserInitials) {
        this.commitOrderTicketuserInitials = commitOrderTicketuserInitials;
    }

    /**
     * This method is called when the component is initialized, but no state is persisted.
     */
    @Override
    public void noStateLoaded() {

    }

    /**
     * If class also is a component, then this method will be called after loading state (even if not state) but only once throughout the life cycle
     */
    @Override
    public void initializeComponent() {

    }
}
