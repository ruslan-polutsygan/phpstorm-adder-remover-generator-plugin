package com.ruslanpolutsygan.adderremover;

import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocParamTag;
import com.jetbrains.php.lang.psi.elements.Field;
import com.intellij.openapi.util.text.StringUtil;

import java.util.TreeSet;

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
