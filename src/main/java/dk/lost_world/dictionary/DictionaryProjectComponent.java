package dk.lost_world.dictionary;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.spellchecker.settings.SpellCheckerSettings;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DictionaryProjectComponent implements ProjectComponent {

    private Project project;

    @Contract(pure = true)
    public DictionaryProjectComponent(Project project) {
        this.project = project;
    }

    @Override
    public void projectOpened() {

        SpellCheckerSettings settings = SpellCheckerSettings.getInstance(project);
        List<String> paths = settings.getCustomDictionariesPaths();

        GlobalSearchScope scope = GlobalSearchScope.allScope(project);

        //FilenameIndex.getAllFilesByExt(project, "dic", scope).forEach(System.out::println);
        // Get all files named project.dic and filter away the ones already added
        Collection<String> filePaths = FilenameIndex
            .getVirtualFilesByName(project, "project.dic", false, scope)
            .stream()
            .map(VirtualFile::getPath)
            .filter(path -> !paths.contains(path))
            .collect(Collectors.toList());

        if (!filePaths.isEmpty()) {
            Notifications.Bus.notify(
                new Notification(
                    "Dictionary register",
                    "Found dictionaries",
                    "Registered the following dictionaries ["+ String.join("", filePaths)+"]",
                    NotificationType.INFORMATION
                ),
                project
            );

            // Add the file paths to the settings and filter away the onces that always is there.
            paths.addAll(
                filePaths
            );
        }
    }

    @Override
    public void projectClosed() {

    }

    @Override
    public void initComponent() {

    }

    @Override
    public void disposeComponent() {

    }

    @NotNull
    @Override
    public String getComponentName() {
        return "Dictionary";
    }
}
