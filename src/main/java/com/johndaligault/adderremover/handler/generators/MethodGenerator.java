package com.johndaligault.adderremover.handler.generators;

import com.jetbrains.php.lang.psi.elements.Field;
import com.johndaligault.adderremover.handler.PhpMethodData;

public abstract class MethodGenerator {
    public abstract PhpMethodData[] generate(Field field);
}
