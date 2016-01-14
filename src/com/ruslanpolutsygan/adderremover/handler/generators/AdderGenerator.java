package com.ruslanpolutsygan.adderremover.handler.generators;

import com.jetbrains.php.lang.psi.elements.Field;
import com.ruslanpolutsygan.adderremover.Util;
import com.ruslanpolutsygan.adderremover.ft.AdderRemoverTemplateDescriptorFactory;

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
