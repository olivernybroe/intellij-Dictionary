package dk.lost_world.dictionary;

import com.intellij.openapi.vfs.*;
import org.jetbrains.annotations.NotNull;

public class DictionaryFileListener implements VirtualFileListener {
    private Dictionary dictionary;

    public DictionaryFileListener(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    @Override
    public void propertyChanged(@NotNull VirtualFilePropertyEvent event) {
        if(event.getPropertyName().equals(VirtualFile.PROP_NAME)) {
            if(event.getFileName().equals("project.dic")) {
                dictionary.registerAndNotify(event.getFile());
            }

            if(event.getOldValue().equals("project.dic")) {
                String path = event.getFile().getPath().substring(0, event.getFile().getPath().length() - ((String)event.getNewValue()).length())+event.getOldValue();
                VirtualFile file = new FakeVirtualFile(path);
                dictionary.removeAndNotify(file);
            }
        }
    }

    @Override
    public void fileCreated(@NotNull VirtualFileEvent event) {
        if(event.getFileName().equals("project.dic")) {
            dictionary.registerAndNotify(event.getFile());
        }
    }

    @Override
    public void fileDeleted(@NotNull VirtualFileEvent event) {
        if(event.getFileName().equals("project.dic")) {
            dictionary.removeAndNotify(event.getFile());
        }
    }

    @Override
    public void fileMoved(@NotNull VirtualFileMoveEvent event) {
        dictionary.moveAndNotify(
            new FakeVirtualFile(event.getOldParent().getPath()+"/"+event.getFileName()),
            event.getFile()
        );
    }

    @Override
    public void fileCopied(@NotNull VirtualFileCopyEvent event) {
        if(event.getFileName().equals("project.dic")) {
            dictionary.registerAndNotify(event.getFile());
        }
    }
}
