package com.johndaligault.adderremover.ft;

import com.intellij.ide.fileTemplates.FileTemplateDescriptor;
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptor;
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptorFactory;
import com.jetbrains.php.lang.PhpFileType;

public class AdderRemoverTemplateDescriptorFactory implements FileTemplateGroupDescriptorFactory {
    public static final String ADDER_TEMPLATE_NAME = "Adder";
    public static final String REMOVER_TEMPLATE_NAME = "Remover";

    public FileTemplateGroupDescriptor getFileTemplatesDescriptor() {
        final FileTemplateDescriptor adder = new FileTemplateDescriptor(ADDER_TEMPLATE_NAME, PhpFileType.INSTANCE.getIcon());
        final FileTemplateDescriptor remover = new FileTemplateDescriptor(REMOVER_TEMPLATE_NAME, PhpFileType.INSTANCE.getIcon());

        return new FileTemplateGroupDescriptor("Adder/Remover", PhpFileType.INSTANCE.getIcon(), adder, remover);
    }
}
