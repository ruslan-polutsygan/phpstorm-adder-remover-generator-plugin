package com.ruslanpolutsygan.adderremover;

import com.intellij.openapi.util.text.StringUtil;

public class Util {
    public static String createAdderName(String fieldName) {
        return "add" + StringUtil.capitalize(
                Util.pluralToSingular(fieldName).replace("_", "")
        );
    }

    public static String createRemoverName(String fieldName) {
        return "remove" + StringUtil.capitalize(
                Util.pluralToSingular(fieldName).replace("_", "")
        );
    }

    public static String createParamName(String fieldName) {
        return "$" + Util.pluralToSingular(fieldName);
    }

    private static String pluralToSingular(String plural) {
        if(plural.endsWith("ies")) {
            return plural.substring(0, plural.length() - 3) + 'y';
        }
        if(plural.endsWith("sses")) {
            return plural.substring(0, plural.length() - 2);
        }
        if(plural.endsWith("s")) {
            return plural.substring(0, plural.length() - 1);
        }
        if(plural.toLowerCase().endsWith("list")) {
            return plural.substring(0, plural.length() - 4);
        }

        return plural;
    }
}
