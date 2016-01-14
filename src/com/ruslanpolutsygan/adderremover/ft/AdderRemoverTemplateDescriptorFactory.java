package com.ruslanpolutsygan.adderremover.ft;

import com.intellij.ide.fileTemplates.FileTemplateDescriptor;
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptor;
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptorFactory;
import com.jetbrains.php.lang.PhpFileType;

public class AdderRemoverTemplateDescriptorFactory implements FileTemplateGroupDescriptorFactory {
    public FileTemplateGroupDescriptor getFileTemplatesDescriptor() {
        final FileTemplateDescriptor adder = new FileTemplateDescriptor("Adder", PhpFileType.INSTANCE.getIcon());
        final FileTemplateDescriptor remover = new FileTemplateDescriptor("Remover", PhpFileType.INSTANCE.getIcon());

        return new FileTemplateGroupDescriptor("Adder/Remover", PhpFileType.INSTANCE.getIcon(), adder, remover);
    }
}
