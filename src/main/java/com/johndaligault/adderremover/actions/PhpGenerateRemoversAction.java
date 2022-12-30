package com.johndaligault.adderremover.actions;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.actions.CodeInsightAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.johndaligault.adderremover.handler.ActionHandler;
import com.johndaligault.adderremover.handler.checkers.RemoverChecker;
import com.johndaligault.adderremover.handler.generators.RemoverGenerator;
import org.jetbrains.annotations.NotNull;


public class PhpGenerateRemoversAction extends CodeInsightAction {
    private final ActionHandler handler;

    public PhpGenerateRemoversAction() {
        this.handler = new ActionHandler(
                new RemoverGenerator(),
                new RemoverChecker()
        );
    }

    @NotNull
    @Override
    protected CodeInsightActionHandler getHandler() {
        return this.handler;
    }

    @Override
    protected boolean isValidForFile(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
        return this.handler.isValidFor(editor, file);
    }
}
