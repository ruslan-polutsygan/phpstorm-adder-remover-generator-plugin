package com.johndaligault.adderremover.handler.generators;

import com.jetbrains.php.lang.psi.elements.Field;
import com.johndaligault.adderremover.Util;
import com.johndaligault.adderremover.ft.AdderRemoverTemplateDescriptorFactory;

public class AdderGenerator extends TemplateBasedMethodGenerator {
    @Override
    protected String getMethodName(Field field) {
        return Util.createAdderName(field.getName());
    }

    @Override
    protected String getMethodTemplateName() {
        return AdderRemoverTemplateDescriptorFactory.ADDER_TEMPLATE_NAME;
    }
}
