package com.johndaligault.adderremover.renamers;

import com.intellij.psi.PsiElement;
import com.intellij.refactoring.rename.naming.AutomaticRenamer;
import com.intellij.refactoring.rename.naming.AutomaticRenamerFactory;
import com.intellij.usageView.UsageInfo;
import com.jetbrains.php.lang.PhpLangUtil;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocProperty;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.Parameter;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.refactoring.PhpRefactoringSettings;

import java.util.*;

import com.johndaligault.adderremover.Util;
import org.jetbrains.annotations.NotNull;

public class FieldAdderRemoverRenamerFactory implements AutomaticRenamerFactory {
    public FieldAdderRemoverRenamerFactory() {
    }

    public boolean isApplicable(@NotNull PsiElement element) {
        return element instanceof Field && !(element instanceof PhpDocProperty) && !((Field)element).isConstant();
    }

    public String getOptionName() {
        return "Rename adder remover";
    }

    public boolean isEnabled() {
        return PhpRefactoringSettings.getInstance().RENAME_FIELD_ACCESSORS;
    }

    public void setEnabled(boolean enabled) {
        PhpRefactoringSettings.getInstance().RENAME_FIELD_ACCESSORS = enabled;
    }

    public @NotNull AutomaticRenamer createRenamer(PsiElement element, String newName, Collection<UsageInfo> usages) {
        return new FieldAdderRemoverRenamer((Field)element, newName);
    }

    public static class FieldAdderRemoverRenamer extends AutomaticRenamer {
        public FieldAdderRemoverRenamer(@NotNull Field field, @NotNull String newName) {
            super();
            PhpClass phpClass = field.getContainingClass();
            if (phpClass != null) {
                String fieldName = field.getName();

                Method[] adderRemoverMethods = findAdderRemoverMethods(phpClass, field);
                this.myElements.addAll(Arrays.asList(adderRemoverMethods));

                for (Method method : adderRemoverMethods) {
                    Parameter[] parameters = method.getParameters();
                    if (parameters.length == 1 && PhpLangUtil.equalsFieldNames(parameters[0].getName(), fieldName)) {
                        this.myElements.add(parameters[0]);
                    }
                }

                this.suggestAllNames(fieldName, newName);
            }
        }

        public Method[] findAdderRemoverMethods(PhpClass phpClass, Field field) {
            List<Method> accessMethods = new ArrayList<Method>();
            boolean isStatic = field.getModifier().isStatic();
            if (phpClass != null) {
                String[] expectedNames = new String[]{
                        Util.createAdderName(field.getName()),
                        Util.createRemoverName(field.getName()),
                };
                Method[] methods = phpClass.getOwnMethods();

                for (Method m : methods) {
                    if (!m.getAccess().isPrivate() && m.getModifier().isStatic() == isStatic) {
                        String methodName = m.getName();

                        for (String expectedName : expectedNames) {
                            if (methodName.equalsIgnoreCase(expectedName)) {
                                accessMethods.add(m);
                                break;
                            }
                        }
                    }
                }
            }

            return accessMethods.toArray(Method.EMPTY);
        }

        public String getDialogTitle() {
            return "Rename Adder/Remover";
        }

        public String getDialogDescription() {
            return "Rename property adder/remover with the following names to:";
        }

        public String entityName() {
            return "Adder/Remover";
        }

        public boolean isSelectedByDefault() {
            return true;
        }
    }
}
