package com.ruslanpolutsygan.adderremover.handler.checkers;

import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.ruslanpolutsygan.adderremover.Util;

public class RemoverChecker extends MethodChecker {
    @Override
    public boolean hasMethod(Field field) {
        PhpClass phpClass = field.getContainingClass();
        if(phpClass == null) {
            return false;
        }

        String methodName = Util.createRemoverName(field.getName());
        Method[] methods = phpClass.getOwnMethods();

        for (Method method : methods) {
            if (methodName.equals(method.getName())) {
                return true;
            }
        }

        return false;
    }
}
