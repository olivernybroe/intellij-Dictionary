package dk.lost_world.dictionary;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.spellchecker.settings.SpellCheckerSettings;
import org.jetbrains.annotations.Contract;

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
        // Get all files named project.dic.
        Collection<String> filePaths = FilenameIndex
            .getVirtualFilesByName(project, "project.dic", false, scope)
            .stream().map(VirtualFile::getPath).collect(Collectors.toList());


        // Add the file paths to the settings and filter away the onces that always is there.
        paths.addAll(
            filePaths.stream().filter(path -> !paths.contains(path)).collect(Collectors.toList())
        );
    }
}
