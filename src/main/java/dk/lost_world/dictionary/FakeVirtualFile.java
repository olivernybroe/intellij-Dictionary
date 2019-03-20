package dk.lost_world.dictionary;

import com.intellij.openapi.vfs.newvfs.impl.StubVirtualFile;
import org.jetbrains.annotations.NotNull;

public class FakeVirtualFile extends StubVirtualFile {
    private String path;

    public FakeVirtualFile(String path) {
        this.path = path;
    }

    @NotNull
    @Override
    public String getPath() {
        return path;
    }
}
