package com.johndaligault.adderremover.actions;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.actions.CodeInsightAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.johndaligault.adderremover.handler.ActionHandler;
import com.johndaligault.adderremover.handler.checkers.AdderChecker;
import com.johndaligault.adderremover.handler.checkers.ChainedChecker;
import com.johndaligault.adderremover.handler.checkers.MethodChecker;
import com.johndaligault.adderremover.handler.checkers.RemoverChecker;
import com.johndaligault.adderremover.handler.generators.AdderGenerator;
import com.johndaligault.adderremover.handler.generators.ChainedGenerator;
import com.johndaligault.adderremover.handler.generators.MethodGenerator;
import com.johndaligault.adderremover.handler.generators.RemoverGenerator;
import org.jetbrains.annotations.NotNull;


public class PhpGenerateAddersAndRemoversAction extends CodeInsightAction {
    private final ActionHandler handler;

    public PhpGenerateAddersAndRemoversAction() {
        MethodGenerator generator = new ChainedGenerator(new MethodGenerator[] {
                new AdderGenerator(),
                new RemoverGenerator()
        });
        MethodChecker checker = new ChainedChecker(new MethodChecker[] {
                new AdderChecker(),
                new RemoverChecker()
        });

        this.handler = new ActionHandler(generator, checker);
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
