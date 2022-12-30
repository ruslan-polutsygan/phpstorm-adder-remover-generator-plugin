package com.johndaligault.adderremover.handler.generators;

import com.jetbrains.php.lang.psi.elements.Field;
import com.johndaligault.adderremover.handler.PhpMethodData;

import java.util.ArrayList;
import java.util.Collections;

public class ChainedGenerator extends MethodGenerator {

    private final MethodGenerator[] generators;

    public ChainedGenerator(MethodGenerator[] generators) {
        this.generators = generators;
    }

    @Override
    public PhpMethodData[] generate(Field field) {
        ArrayList<PhpMethodData> methods = new ArrayList<>();

        for (MethodGenerator generator : this.generators) {
            Collections.addAll(methods, generator.generate(field));
        }

        return methods.toArray(new PhpMethodData[methods.size()]);
    }
}
