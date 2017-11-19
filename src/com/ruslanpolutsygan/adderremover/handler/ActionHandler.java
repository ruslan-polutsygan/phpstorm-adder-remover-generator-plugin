package com.ruslanpolutsygan.adderremover.handler;

import com.intellij.codeInsight.hint.HintManager;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.tree.IElementType;
import com.jetbrains.php.lang.actions.PhpNamedElementNode;
import com.jetbrains.php.lang.actions.generation.PhpGenerateFieldAccessorHandlerBase;
import com.jetbrains.php.lang.intentions.generators.PhpAccessorMethodData;
import com.jetbrains.php.lang.lexer.PhpTokenTypes;
import com.jetbrains.php.lang.parser.PhpElementTypes;
import com.jetbrains.php.lang.parser.PhpStubElementTypes;
import com.jetbrains.php.lang.psi.PhpCodeEditUtil;
import com.jetbrains.php.lang.psi.PhpFile;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.ruslanpolutsygan.adderremover.Util;
import com.ruslanpolutsygan.adderremover.handler.checkers.MethodChecker;
import com.ruslanpolutsygan.adderremover.handler.generators.MethodGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ActionHandler extends PhpGenerateFieldAccessorHandlerBase {

    private MethodGenerator generator;
    private MethodChecker checker;

    public ActionHandler(MethodGenerator generator, MethodChecker checker) {
        this.generator = generator;
        this.checker = checker;
    }

    public void invoke(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
        PhpClass phpClass = PhpCodeEditUtil.findClassAtCaret(editor, file);
        if(phpClass == null) {
            return;
        }

        PhpNamedElementNode[] fields = this.collectFields(phpClass);
        if(fields.length == 0) {
            if(!ApplicationManager.getApplication().isHeadlessEnvironment()) {
                HintManager.getInstance().showErrorHint(editor, this.getErrorMessage());
            }

            return;
        }

        ArrayList<Field> selectedFields = new ArrayList<>();
        PhpNamedElementNode[] chosenMembers = this.chooseMembers(fields, true, file.getProject());

        if(chosenMembers == null) {
            return;
        }

        for (PhpNamedElementNode member : chosenMembers) {
            PsiElement memberPsi = member.getPsiElement();
            if (memberPsi instanceof Field) {
                selectedFields.add((Field) memberPsi);
            }
        }

        StringBuffer buffer = new StringBuffer();
        int startOffset = ActionHandler.getSuitableEditorPosition(editor, (PhpFile)file);

        for(Field field : selectedFields) {
            for(PhpMethodData methodData : this.generator.generate(field)) {
                if(methodData.getPhpDoc() != null) {
                    buffer.append(methodData.getPhpDoc().getText());
                    buffer.append('\n');
                }
                if(methodData.getMethod() != null) {
                    buffer.append(methodData.getMethod().getText());
                    buffer.append('\n');
                }
            }
        }

        editor.getDocument().insertString(startOffset, buffer);
        int endOffset = startOffset + buffer.length();
        CodeStyleManager.getInstance(project).reformatText(file, startOffset, endOffset);
        PsiDocumentManager.getInstance(project).commitDocument(editor.getDocument());
    }

    @Override
    protected PhpAccessorMethodData[] createAccessors(PsiElement psiElement) {
        return new PhpAccessorMethodData[0];
    }

    @Override
    protected boolean isSelectable(PhpClass phpClass, Field field) {
        return !this.checker.hasMethod(field) && Util.isArrayLikeField(field);
    }

    @NotNull
    protected PhpNamedElementNode[] collectFields(@NotNull PhpClass phpClass) {
        TreeMap<String, PhpNamedElementNode> nodes = new TreeMap<>();
        Collection fields = phpClass.getFields();

        for (Object field1 : fields) {
            Field field = (Field) field1;
            if (!field.isConstant() && this.isSelectable(phpClass, field)) {
                nodes.put(field.getName(), new PhpNamedElementNode(field));
            }
        }

        return nodes.values().toArray(new PhpNamedElementNode[nodes.size()]);
    }

    @Override
    protected String getErrorMessage() {
        return "No suitable properties found";
    }

    @Override
    protected boolean containsSetters() {
        return false;
    }

    public boolean startInWriteAction() {
        return true;
    }

    /** copy-paste from com.jetbrains.php.lang.actions.generation.PhpGenerateFieldAccessorHandlerBase */
    private static int getSuitableEditorPosition(Editor editor, PhpFile phpFile) {
        PsiElement currElement = phpFile.findElementAt(editor.getCaretModel().getOffset());
        if(currElement != null) {
            PsiElement parent = currElement.getParent();

            for(PsiElement prevParent = currElement; parent != null && !(parent instanceof PhpFile); parent = parent.getParent()) {
                if(isClassMember(parent)) {
                    return getNextPos(parent);
                }

                if(parent instanceof PhpClass) {
                    while(prevParent != null) {
                        if(isClassMember(prevParent) || prevParent.getNode().getElementType() == PhpTokenTypes.chLBRACE) {
                            return getNextPos(prevParent);
                        }

                        prevParent = prevParent.getPrevSibling();
                    }

                    for(PsiElement classChild = parent.getFirstChild(); classChild != null; classChild = classChild.getNextSibling()) {
                        if(classChild.getNode().getElementType() == PhpTokenTypes.chLBRACE) {
                            return getNextPos(classChild);
                        }
                    }
                }

                prevParent = parent;
            }
        }

        return -1;
    }

    private static boolean isClassMember(PsiElement element) {
        if(element == null) {
            return false;
        } else {
            IElementType elementType = element.getNode().getElementType();
            return elementType == PhpElementTypes.CLASS_FIELDS || elementType == PhpElementTypes.CLASS_CONSTANTS || elementType == PhpStubElementTypes.CLASS_METHOD;
        }
    }

    private static int getNextPos(PsiElement element) {
        PsiElement next = element.getNextSibling();
        return next != null?next.getTextOffset():-1;
    }
}
