package com.ruslanpolutsygan.adderremover.handler.generators;

import com.jetbrains.php.lang.PhpCodeUtil;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocParamTag;
import com.jetbrains.php.lang.intentions.generators.PhpAccessorMethodData;
import com.jetbrains.php.lang.psi.PhpPsiElementFactory;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.Method;

import java.util.TreeSet;

public abstract class TemplateBasedMethodGenerator extends MethodGenerator {
    @Override
    public PhpAccessorMethodData[] generate(Field field) {

        String methodTemplate = this.getMethodTemplate(field);
        String methodPhpDocTemplate = this.getPhpDocTemplate(field);

        Method method = PhpCodeUtil.createMethodFromTemplate(field.getProject(), false, methodTemplate);
        PhpDocComment methodPhpDoc = PhpPsiElementFactory.createFromText(field.getProject(), PhpDocComment.class, methodPhpDocTemplate);

        return new PhpAccessorMethodData[] {
                new PhpAccessorMethodData(field.getContainingClass(), methodPhpDoc, method)
        };
    }

    protected abstract String getMethodTemplate(Field field);
    protected abstract String getPhpDocTemplate(Field field);

    protected static String getTypeHint(Field field) {
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

    protected static String getMethodArgument(Field field) {
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

    protected boolean isDoctrineCollectionField(Field field) {
        PhpDocComment phpDoc = field.getDocComment();
        if(phpDoc == null) {
            return false;
        }

        PhpDocParamTag varTag = phpDoc.getVarTag();
        if(varTag == null) {
            return false;
        }

        TreeSet<String> types = (TreeSet<String>) varTag.getType().getTypes();

        return types.contains("\\Doctrine\\Common\\Collections\\Collection");
    }
}
