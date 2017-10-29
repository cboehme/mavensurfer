package de.dnb.tools.svnfairy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.io.SVNRepository;

public class SubversionPomFile implements PomFile {

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

}
