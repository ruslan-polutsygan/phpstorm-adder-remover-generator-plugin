package com.ruslanpolutsygan.adderremover.handler.generators;

import com.jetbrains.php.lang.PhpCodeUtil;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocParamTag;
import com.jetbrains.php.lang.intentions.generators.PhpAccessorMethodData;
import com.jetbrains.php.lang.psi.PhpPsiElementFactory;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.Method;
import com.ruslanpolutsygan.adderremover.Util;

import java.util.TreeSet;

public class RemoverGenerator extends MethodGenerator {
    private final static String REMOVER_TEMPLATE = "public function __METHOD_NAME__(__ARGUMENT__)\n" +
            "{\n" +
            "\t\t// @todo: add method body there\n" +
            "}\n";

    private final static String REMOVER_PHP_DOC_TEMPLATE = "/**\n @param __TYPE_HINT__ $var\n */";

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

    private static String getMethodArgument(Field field) {
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
        String methodTemplate = RemoverGenerator.REMOVER_TEMPLATE
                .replace("__METHOD_NAME__", Util.createRemoverName(field.getName()))
                .replace("__FIELD_NAME__", field.getName())
                .replace("__ARGUMENT__", RemoverGenerator.getMethodArgument(field))
                ;
        String phpDocTemplate = RemoverGenerator.REMOVER_PHP_DOC_TEMPLATE
                .replace("__TYPE_HINT__", RemoverGenerator.getTypeHint(field))
                ;

        Method method = PhpCodeUtil.createMethodFromTemplate(field.getProject(), false, methodTemplate);
        PhpDocComment methodDoc = PhpPsiElementFactory.createFromText(field.getProject(), PhpDocComment.class, phpDocTemplate);

        return new PhpAccessorMethodData[] {
                new PhpAccessorMethodData(field.getContainingClass(), methodDoc, method)
        };
    }
}
