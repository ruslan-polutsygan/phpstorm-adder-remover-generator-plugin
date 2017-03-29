package com.ruslanpolutsygan.adderremover;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocParamTag;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.PhpClass;

import java.util.Collection;
import java.util.HashSet;
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
        PhpDocComment phpDoc = field.getDocComment();
        if(phpDoc == null) {
            return false;
        }

        PhpDocParamTag varTag = phpDoc.getVarTag();
        if(varTag == null) {
            return false;
        }

        if(Util.isDoctrineCollectionField(field)) {
            return true;
        }

        Set<String> types = varTag.getType().getTypes();
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
        PhpDocComment phpDoc = field.getDocComment();
        if(phpDoc == null) {
            return false;
        }

        PhpDocParamTag varTag = phpDoc.getVarTag();
        if(varTag == null) {
            return false;
        }

        Set<String> types = varTag.getType().getTypes();
        Set<String> doctrineClassNames = Util.getDoctrineCollectionClassNames(field.getProject());

        types.retainAll(doctrineClassNames);

        return types.size()>0;
    }

    private static Set<String> getDoctrineCollectionClassNames(Project project) {
        String doctrineCollectionInterfaceFQN = "\\Doctrine\\Common\\Collections\\Collection";

        PhpIndex index = PhpIndex.getInstance(project);
        Collection<PhpClass> doctrineCollectionClasses = index.getAllSubclasses(doctrineCollectionInterfaceFQN);

        Set<String> classNames = new HashSet<>();

        for(PhpClass c: doctrineCollectionClasses) {
            classNames.add("\\" + c.getPresentableFQN());
        }
        classNames.add(doctrineCollectionInterfaceFQN);

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
