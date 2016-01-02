package com.ruslanpolutsygan.adderremover.handler.generators;

import com.intellij.openapi.util.text.StringUtil;
import com.jetbrains.php.lang.PhpCodeUtil;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocParamTag;
import com.jetbrains.php.lang.intentions.generators.PhpAccessorMethodData;
import com.jetbrains.php.lang.psi.PhpPsiElementFactory;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.Method;
import com.ruslanpolutsygan.adderremover.Util;

import java.util.TreeSet;

public class AdderGenerator extends MethodGenerator {
    private final static String ADDER_TEMPLATE = "public function __METHOD_NAME__(__ARGUMENT__)" +
            "{" +
            "$this->__FIELD_NAME__[] = $var;" +
            "}";

    private final static String ADDER_PHP_DOC_TEMPLATE = "/**\n @param __TYPE_HINT__ $var\n */";

    private static String getTypeHint(Field field) {
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

    private static String getAdderArgument(Field field) {
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


    @Override
    public PhpAccessorMethodData[] generate(Field field) {
        String adderTemplate = AdderGenerator.ADDER_TEMPLATE
                .replace("__METHOD_NAME__", Util.createAdderName(field.getName()))
                .replace("__FIELD_NAME__", field.getName())
                .replace("__ARGUMENT__", AdderGenerator.getAdderArgument(field))
                ;
        String adderPhpDocTemplate = AdderGenerator.ADDER_PHP_DOC_TEMPLATE
                .replace("__TYPE_HINT__", AdderGenerator.getTypeHint(field))
                ;

        Method adder = PhpCodeUtil.createMethodFromTemplate(field.getProject(), false, adderTemplate);
        PhpDocComment adderDoc = PhpPsiElementFactory.createFromText(field.getProject(), PhpDocComment.class, adderPhpDocTemplate);

        return new PhpAccessorMethodData[] {
                new PhpAccessorMethodData(field.getContainingClass(), adderDoc, adder)
        };
    }
}
