package com.johndaligault.adderremover;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocParamTag;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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

    public static boolean isArrayLikeField(Field field) {
        // check PhpDoc existence First
        PhpDocComment phpDoc = field.getDocComment();
        if(phpDoc == null) {
            // Fallback to field typing
            return Util.isDoctrineCollectionField(field) || isTypedArrayLikeField(field.getDeclaredType());
        }

        // check PhpDoc @var First
        PhpDocParamTag varTag = phpDoc.getVarTag();
        if(varTag == null) {
            // Fallback to field typing
            return Util.isDoctrineCollectionField(field) || isTypedArrayLikeField(field.getDeclaredType());
        }

        return Util.isDoctrineCollectionField(field) || isTypedArrayLikeField(varTag.getType());
    }

    public static boolean isTypedArrayLikeField(PhpType typeField) {
        Set<String> types = typeField.getTypes();
        for (String type : types) {
            if (type.endsWith("[]")) {
                return true;
            }

            if(type.equals("\\array")) {
                return true;
            }
        }

        return false;
    }

    public static boolean isDoctrineCollectionField(Field field) {
        // check PhpDoc existence First
        PhpDocComment phpDoc = field.getDocComment();
        if(phpDoc == null) {
            // Fallback to field typing
            return isTypedDoctrineCollectionField(field, field.getType());
        }

        // check PhpDoc @var First
        PhpDocParamTag varTag = phpDoc.getVarTag();
        return isTypedDoctrineCollectionField(field, Objects.requireNonNullElse(varTag, field).getType());

    }

    public static boolean isTypedDoctrineCollectionField(Field field, PhpType typeField) {
        Set<String> types = typeField.getTypes();
        Set<String> doctrineClassNames = Util.getDoctrineCollectionClassNames(field.getProject());

        types.retainAll(doctrineClassNames);

        return types.size()>0;
    }

    private static Set<String> getDoctrineCollectionClassNames(Project project) {
        Set<String> classNames = new HashSet<>();

        classNames.add("\\Doctrine\\Common\\Collections\\Collection");
        classNames.add("\\Doctrine\\Common\\Collections\\ArrayCollection");
        classNames.add("\\Doctrine\\Common\\Collections\\AbstractLazyCollection");

        return classNames;
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
