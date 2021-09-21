package com.mobiquity.utils;

import lombok.AllArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public class FileUtils {
    String filePath;

    public List<String> readByLine() throws IOException {
        Path path = Paths.get(filePath);

        Stream<String> lines = Files.lines(path);
        List<String> collect1 = lines.collect(Collectors.toList());
        lines.close();

        return collect1;
    }
}
