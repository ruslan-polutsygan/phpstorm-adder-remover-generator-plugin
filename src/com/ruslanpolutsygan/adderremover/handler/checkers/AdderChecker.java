package com.ruslanpolutsygan.adderremover.handler.checkers;

import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.ruslanpolutsygan.adderremover.Util;

public class AdderChecker extends MethodChecker {
    @Override
    public boolean hasMethod(Field field) {
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
}
