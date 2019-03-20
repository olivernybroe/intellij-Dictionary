package dk.lost_world.dictionary;

import com.intellij.openapi.vfs.*;
import org.jetbrains.annotations.NotNull;

public class DictionaryFileListener implements VirtualFileListener {
    private Dictionary dictionary;

    public DictionaryFileListener(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    /**
     * Fired when a virtual file is renamed from within IDEA, or its writable status is changed.
     * For files renamed externally, {@link #fileCreated} and {@link #fileDeleted} events will be fired.
     *
     * @param event the event object containing information about the change.
     */
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

    /**
     * Fired when a virtual file is created. This event is not fired for files discovered during initial VFS initialization.
     *
     * @param event the event object containing information about the change.
     */
    @Override
    public void fileCreated(@NotNull VirtualFileEvent event) {
        if(event.getFileName().equals("project.dic")) {
            dictionary.registerAndNotify(event.getFile());
        }
    }

    /**
     * Fired when a virtual file is deleted.
     *
     * @param event the event object containing information about the change.
     */
    @Override
    public void fileDeleted(@NotNull VirtualFileEvent event) {
        if(event.getFileName().equals("project.dic")) {
            dictionary.removeAndNotify(event.getFile());
        }
    }

    /**
     * Fired when a virtual file is moved from within IDEA.
     *
     * @param event the event object containing information about the change.
     */
    @Override
    public void fileMoved(@NotNull VirtualFileMoveEvent event) {
        dictionary.moveAndNotify(
            new FakeVirtualFile(event.getOldParent().getPath()+"/"+event.getFileName()),
            event.getFile()
        );
    }

    /**
     * Fired when a virtual file is copied from within IDEA.
     *
     * @param event the event object containing information about the change.
     */
    @Override
    public void fileCopied(@NotNull VirtualFileCopyEvent event) {
        if(event.getFileName().equals("project.dic")) {
            dictionary.registerAndNotify(event.getFile());
        }
    }
}
