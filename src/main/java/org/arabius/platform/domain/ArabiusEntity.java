package org.arabius.platform.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class ArabiusEntity {
    private String separator = "|";

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    protected ArrayList<Integer> parseStringToIntList(String input) {
        String[] parts = input.split(separator);
        ArrayList<Integer> result = new ArrayList<>();
        for (String part : parts) {
            result.add(Integer.parseInt(part.trim()));
        }
        return result;
    }

    protected List<String> parseStringToStringList(String stringToParse) {
        return Arrays.asList(stringToParse.split(separator));
    }
}