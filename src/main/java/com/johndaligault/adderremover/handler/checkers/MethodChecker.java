package com.johndaligault.adderremover.handler.checkers;

import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import org.jetbrains.annotations.Nullable;

public abstract class MethodChecker {
    public abstract boolean hasMethod(Field field);

    protected boolean hasMethod(String methodName, @Nullable PhpClass phpClass) {
        if(phpClass == null) {
            return false;
        }

        for (Method method : phpClass.getOwnMethods()) {
            if (methodName.equals(method.getName())) {
                return true;
            }
        }

        return false;
    }

}
