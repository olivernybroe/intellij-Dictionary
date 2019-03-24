package dk.lost_world.dictionary;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class DictionaryProjectComponent implements ProjectComponent {
    private Project project;

    public DictionaryProjectComponent(Project project) {
        this.project = project;
    }

    @Override
    public void projectOpened() {
        Dictionary dictionary = new Dictionary(project);

        dictionary.registerAndNotify(
            FilenameIndex.getVirtualFilesByName(
                project,
                "project.dic",
                false,
                GlobalSearchScope.allScope(project)
            )
        );

        VirtualFileManager.getInstance().addVirtualFileListener(
            new DictionaryFileListener(dictionary)
        );
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
