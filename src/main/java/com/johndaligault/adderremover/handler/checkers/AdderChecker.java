package com.johndaligault.adderremover.handler.checkers;
import com.jetbrains.php.lang.psi.elements.Field;
import com.johndaligault.adderremover.Util;

public class AdderChecker extends MethodChecker {
    @Override
    public boolean hasMethod(Field field) {
        return this.hasMethod(
                Util.createAdderName(field.getName()), field.getContainingClass()
        );
    }
}
