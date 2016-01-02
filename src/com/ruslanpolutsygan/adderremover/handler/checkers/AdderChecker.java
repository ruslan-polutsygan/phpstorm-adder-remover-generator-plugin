package com.ruslanpolutsygan.adderremover.handler.checkers;

import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.ruslanpolutsygan.adderremover.Util;

public class AdderChecker extends MethodChecker {
    @Override
    public boolean hasMethod(Field field) {
        return this.hasMethod(
                Util.createAdderName(field.getName()), field.getContainingClass()
        );
    }
}
