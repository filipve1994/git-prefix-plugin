package be.filip.intellij.settings;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.regex.Pattern;

public class PluginSettingsConfigurable implements SearchableConfigurable {

    private static final Logger logger = LoggerFactory.getLogger(PluginSettingsConfigurable.class);

    private Pattern allowedCharsPattern = Pattern.compile("[ :\\_\\-/\\|,\\.]+");
    private PluginSettingsForm settingsForm;


    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Git Auto Prefix Filip";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        if (settingsForm == null) {
            settingsForm = new PluginSettingsForm();
        }
        reset();
        return settingsForm.getPanel();
    }

//    @Override
//    public boolean isModified() {
//        PluginSettings settings = PluginSettings.getInstance();
//        String oldValue = settings.getCommitMessageDelimiter();
//        String newValue = settingsForm.getDelimiter();
//        return !Objects.equals(oldValue, newValue);
//    }

    @Override
    public boolean isModified() {
        PluginSettings settings = PluginSettings.getInstance();

//        boolean modified = !Objects.equals(settings.getCommitMessageDelimiter(), settingsForm.getDelimiter());
//        modified |= settingsForm.getTxtUserInitials().equals(settings.getUserInitials());

        boolean modified = settingsForm.getTxtUserInitials().equals(settings.getUserInitials());
        modified |= settingsForm.getDelimiter().equals(settings.getCommitMessageDelimiter());
        modified |= settingsForm.getCheckedOrder() != settings.isCommitOrderTicketuserInitials();
//        modified |= !Objects.equals(settings.getCommitMessageDelimiter(), settingsForm.getDelimiter());

        return modified;
    }

    /**
     * Default values will not be saved in settingsplugin.xml but will keep working from memory when loading plugin in
     * @throws ConfigurationException
     */
    @Override
    public void apply() throws ConfigurationException {
        if (settingsForm != null) {
            if (isModified()) {
                validate();
                PluginSettings settings = PluginSettings.getInstance();

                settings.setCommitMessageDelimiter(settingsForm.getDelimiter());
                settings.setUserInitials(settingsForm.getTxtUserInitials());
                settings.setCommitOrderTicketuserInitials(settingsForm.getCheckedOrder());

                settings.save();
            }
        }
    }

    private void validate() throws ConfigurationException {

        if (settingsForm.getDelimiter().isEmpty()) {
            throw new ConfigurationException("Delimiter must not be empty", "Validation failed");
        }

        if (!allowedCharsPattern.matcher(settingsForm.getDelimiter()).matches()) {
            throw new ConfigurationException("Delimiter can only contain following chars: \" :_-/|,.\"", "Validation failed");
        }
    }

    @Override
    public void reset() {
        if (settingsForm != null) {
            settingsForm.resetEditorFrom(PluginSettings.getInstance());
        }
    }

    @NotNull
    @Override
    public String getId() {
        return "git.auto.prefix";
    }


    @Nullable
    @Override
    public Runnable enableSearch(String option) {
        return null;
    }
}
