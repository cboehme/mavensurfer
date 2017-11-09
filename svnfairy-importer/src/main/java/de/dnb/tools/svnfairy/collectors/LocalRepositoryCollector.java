package de.dnb.tools.svnfairy.collectors;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

import de.dnb.tools.svnfairy.model.PomFile;

public class LocalRepositoryCollector implements PomCollector {

    private final Path repositoryDir;

    public LocalRepositoryCollector(Path repositoryDir) {
        this.repositoryDir = repositoryDir;
    }

    @Override
    public Iterable<PomFile> getPoms() {
        try {
            return Files.walk(repositoryDir)
                    .filter(path -> path.toFile().isFile())
                    .filter(path -> path.toString().endsWith(".pom"))
                    .map(LocalPomFile::new)
                    .collect(toList());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
