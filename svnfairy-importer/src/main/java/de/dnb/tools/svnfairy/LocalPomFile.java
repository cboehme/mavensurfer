package de.dnb.tools.svnfairy;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public final class LocalPomFile implements PomFile {

    private final Path path;

    LocalPomFile(Path path) {
        this.path = path;
    }

    @Override
    public String getName() {
        return path.toString();
    }

    @Override
    public byte[] getContents() {
        try (
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ) {
            Files.copy(path, byteStream);
            return byteStream.toByteArray();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        return Util.equals(this, obj, (a, b) ->
                Objects.equals(a.path, b.path));
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }

    @Override
    public String toString() {
        return path.toString();
    }

}
