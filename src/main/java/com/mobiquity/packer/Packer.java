package com.mobiquity.packer;

import com.mobiquity.exception.APIException;
import com.mobiquity.packer.model.Package;
import com.mobiquity.utils.BestPackageOptionsUtils;
import com.mobiquity.utils.FileUtils;
import com.mobiquity.utils.LinesParser;
import com.mobiquity.utils.config.Config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Packer {

    private Packer() {
    }

    public static String pack(String filePath) throws APIException {
        Config.init("application.properties");

        FileUtils fileReaderer = new FileUtils(filePath);
        List<String> linesFromFile = new ArrayList<>();
        try {
            linesFromFile = fileReaderer.readByLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (linesFromFile.isEmpty()) {
            throw new APIException("File is empty");
        }
        List<Package> packages = LinesParser.parseLines(linesFromFile);

        return packages.stream()
                .map(BestPackageOptionsUtils::suitableItemIndexes)
                .collect(Collectors.joining("\n"));
    }

    public static void main(String[] args) throws IOException {
        try {
            String pack = pack("C:\\Users\\Julia\\Desktop\\docs\\test1.txt");
            System.out.printf(pack);
        } catch (APIException e) {
            e.printStackTrace();
        }
    }
}