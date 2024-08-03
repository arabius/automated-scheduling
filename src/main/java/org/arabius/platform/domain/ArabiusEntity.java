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

    protected ArrayList<Integer> parseStringToIntList(String stringToParse) {
        ArrayList<Integer> integerList = new ArrayList<>();
        String[] stringArray = stringToParse.split("\\"+separator);
        for (String str : stringArray) {
            if (str.matches("\\d+")) {
                integerList.add(Integer.parseInt(str));
            }
        }
        return integerList;
    }

    protected ArrayList<String> parseStringToStringList(String stringToParse) {
        return new ArrayList<>(Arrays.asList(stringToParse.split(separator)));
    }
}