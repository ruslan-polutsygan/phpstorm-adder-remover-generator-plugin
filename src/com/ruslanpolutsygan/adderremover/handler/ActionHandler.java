package com.ruslanpolutsygan.adderremover.handler;

import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.actions.generation.PhpGenerateFieldAccessorHandlerBase;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocParamTag;
import com.jetbrains.php.lang.intentions.generators.PhpAccessorMethodData;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.ruslanpolutsygan.adderremover.handler.checkers.MethodChecker;
import com.ruslanpolutsygan.adderremover.handler.generators.MethodGenerator;

import java.util.TreeSet;

public class ActionHandler extends PhpGenerateFieldAccessorHandlerBase {

    private MethodGenerator generator;
    private MethodChecker checker;

    public ActionHandler(MethodGenerator generator, MethodChecker checker) {
        this.generator = generator;
        this.checker = checker;
    }

    @Override
    protected PhpAccessorMethodData[] createAccessors(PsiElement psiElement) {
        return this.generator.generate((Field) psiElement);
    }

    @Override
    protected boolean isSelectable(PhpClass phpClass, Field field) {
        return !this.checker.hasMethod(field) && this.hasArrayLikeAnnotation(field);
    }

    private boolean hasArrayLikeAnnotation(Field field) {
        PhpDocComment phpDoc = field.getDocComment();
        if(phpDoc == null) {
            return false;
        }

        PhpDocParamTag varTag = phpDoc.getVarTag();
        if(varTag == null) {
            return false;
        }

        TreeSet<String> types = (TreeSet<String>) varTag.getType().getTypes();
        for (String type : types) {
            if (type.endsWith("[]")) {
                return true;
            }

            if(type.equals("\\array")) {
                return true;
            }

            if(type.equals("\\Doctrine\\Common\\Collections\\Collection")) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected String getErrorMessage() {
        return "No suitable properties found";
    }

    @Override
    protected boolean containsSetters() {
        return false;
    }
}
