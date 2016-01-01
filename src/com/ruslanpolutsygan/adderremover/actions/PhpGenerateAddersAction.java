package com.ruslanpolutsygan.adderremover.actions;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.actions.CodeInsightAction;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.PhpCodeUtil;
import com.jetbrains.php.lang.actions.generation.PhpGenerateFieldAccessorHandlerBase;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocParamTag;
import com.jetbrains.php.lang.intentions.generators.PhpAccessorMethodData;
import com.jetbrains.php.lang.psi.PhpPsiElementFactory;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.ruslanpolutsygan.adderremover.Util;
import org.jetbrains.annotations.NotNull;

import java.util.TreeSet;


public class PhpGenerateAddersAction extends CodeInsightAction {

    private final PhpGenerateFieldAccessorHandlerBase handler = new PhpGenerateFieldAccessorHandlerBase() {

        @Override
        protected PhpAccessorMethodData[] createAccessors(PsiElement psiElement) {
            Field field = (Field) psiElement;

            String adderTemplate = Util.ADDER_TEMPLATE
                    .replace("__METHOD_NAME__", Util.createAdderName(field.getName()))
                    .replace("__FIELD_NAME__", field.getName())
                    .replace("__ARGUMENT__", Util.getAdderArgument(field))
                ;
            String adderPhpDocTemplate = Util.ADDER_PHP_DOC_TEMPLATE
                    .replace("__TYPE_HINT__", Util.getTypeHint(field))
                ;

            Method adder = PhpCodeUtil.createMethodFromTemplate(field.getProject(), false, adderTemplate);
            PhpDocComment adderDoc = PhpPsiElementFactory.createFromText(field.getProject(), PhpDocComment.class, adderPhpDocTemplate);

            return new PhpAccessorMethodData[] {
                    new PhpAccessorMethodData(field.getContainingClass(), adderDoc, adder)
            };
        }

        @Override
        protected boolean isSelectable(PhpClass phpClass, Field field) {
            return !this.hasAdder(field) && this.hasArrayLikeAnnotation(field);
        }

        private boolean hasAdder(Field field) {
            PhpClass phpClass = field.getContainingClass();
            if(phpClass == null) {
                return false;
            }

            String adderName = Util.createAdderName(field.getName());
            Method[] methods = phpClass.getOwnMethods();

            for (Method method : methods) {
                if (adderName.equals(method.getName())) {
                    return true;
                }
            }

            return false;
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

            return types.contains("\\array") || types.contains("\\Doctrine\\Common\\Collections\\Collection");
        }

        @Override
        protected String getErrorMessage() {
            return "No properties to generate adder for";
        }

        @Override
        protected boolean containsSetters() {
            return false;
        }
    };

    @NotNull
    @Override
    protected CodeInsightActionHandler getHandler() {
        return this.handler;
    }
}
