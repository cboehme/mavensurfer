package de.dnb.tools.svnfairy.collectors;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.io.SVNRepository;

import de.dnb.tools.svnfairy.Util;
import de.dnb.tools.svnfairy.model.PomFile;

class SubversionPomFile implements PomFile {

    private final SVNRepository repository;
    private final String path;

    SubversionPomFile(SVNRepository repository, String path) {
        this.repository = repository;
        this.path = path;
    }

    @Override
    public String getName() {
        return path;
    }

    @Override
    public byte[] getContents() {
        try (
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ) {
            repository.getFile(path, -1, null, byteStream);
            return byteStream.toByteArray();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (SVNException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        return Util.equals(this, obj, (a, b) ->
                Objects.equals(a.repository.getLocation(),
                        b.repository.getLocation()) &&
                Objects.equals(a.path, b.path));
    }

    @Override
    public int hashCode() {
        return Objects.hash(repository.getLocation(), path);
    }

    @Override
    public String toString() {
        String location = repository.getLocation().toString();
        if (!location.endsWith("/")) {
            location += "/";
        }
        return location + path;
    }

}
