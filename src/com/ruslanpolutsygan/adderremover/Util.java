package com.ruslanpolutsygan.adderremover;

import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocParamTag;
import com.jetbrains.php.lang.psi.elements.Field;
import com.intellij.openapi.util.text.StringUtil;

import java.util.TreeSet;

public class Util {
    public final static String ADDER_TEMPLATE = "public function __METHOD_NAME__(__ARGUMENT__)" +
            "{" +
            "$this->__FIELD_NAME__[] = $var;" +
            "}";

    public final static String ADDER_PHP_DOC_TEMPLATE = "/**\n @param __TYPE_HINT__ $var\n */";

    public static String createAdderName(String fieldName) {
        return "add" + StringUtil.capitalize(
                Util.pluralToSingular(fieldName).replace("_", "")
        );
    }

    public static String getTypeHint(Field field) {
        PhpDocComment phpDoc = field.getDocComment();
        if(phpDoc == null) {
            return "mixed";
        }

        PhpDocParamTag varTag = phpDoc.getVarTag();
        if(varTag == null) {
            return "mixed";
        }

        TreeSet<String> types = (TreeSet<String>) varTag.getType().getTypes();
        for (String type : types) {
            if (type.endsWith("[]")) {
                return /*StringUtil.escapeBackSlashes*/(type.substring(0, type.length() - 2));
            }
        }

        return "mixed";
    }

    public static String getAdderArgument(Field field) {
        PhpDocComment phpDoc = field.getDocComment();
        if(phpDoc == null) {
            return "$var";
        }

        PhpDocParamTag varTag = phpDoc.getVarTag();
        if(varTag == null) {
            return "$var";
        }

        TreeSet<String> types = (TreeSet<String>) varTag.getType().getTypes();
        for (String type : types) {
            if (type.endsWith("[]")) {
                return type.substring(type.lastIndexOf('\\') + 1, type.length() - 2) + " $var";
            }
        }

        return "$var";
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
