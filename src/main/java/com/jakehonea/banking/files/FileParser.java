package com.jakehonea.banking.files;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileParser {

    /**
     * Maps the given file to retrieve the keys and values
     *
     * @param file
     * @return mapped keys and values
     * @throws IOException unable to read or locate the given file
     */
    public static Map<String, String> parseFile(File file)
            throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(file));

        Map<String, String> entries = new HashMap<String, String>();

        String line;

        while ((line = reader.readLine()) != null) {

            String[] parts = line.split("=");

            entries.put(parts[0], parts[1]);

        }

        return entries;

    }

}
