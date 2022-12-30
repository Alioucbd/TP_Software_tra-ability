package spoonTraceability.processors;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtCodeSnippetExpression;
import spoon.reflect.code.CtCodeSnippetStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.reference.CtTypeReference;

import java.util.EnumSet;
import java.util.Locale;
import java.util.logging.Logger;

public class InitSpoon extends AbstractProcessor<CtClass<?>> {

    @Override
    public boolean isToBeProcessed(CtClass e) {
        return e.getQualifiedName().equals("ecommerce.Repository");
    }

    @Override
    public void process(CtClass e) {
        //  Crée les loggers dans la classe
        createLogger(e, "Read");
        createLogger(e, "Write");
    }

    private void createLogger(CtClass<?> e, String name) {
        CtCodeSnippetExpression<Object> snippet = e.getFactory().Core().createCodeSnippetExpression();
        CtTypeReference<Object> fieldType = e.getFactory().createTypeParameterReference(Logger.class.getCanonicalName());
        EnumSet<ModifierKind> modifiers = EnumSet.of(ModifierKind.STATIC, ModifierKind.PRIVATE, ModifierKind.FINAL);

        snippet.setValue("java.util.logging.Logger.getLogger(\"" + name.toLowerCase(Locale.ROOT) + e.getSimpleName() + "\")");
        CtField<?> field = e.getFactory().createField(e, modifiers, fieldType, name.toUpperCase(Locale.ROOT) + "_LOGGER", snippet);

        e.addField(field);
        // Crée un getter pour le logger
        createGetter(e, name);
    }

    private void createGetter(CtClass<?> e, String name) {
        CtCodeSnippetStatement snippet = getFactory().Core().createCodeSnippetStatement();
        CtTypeReference<Object> returnType = e.getFactory().createTypeParameterReference(Logger.class.getCanonicalName());
        EnumSet<ModifierKind> modifiers = EnumSet.of(ModifierKind.STATIC, ModifierKind.PUBLIC);
        CtBlock<?> body = e.getFactory().createBlock();

        snippet.setValue("return " + name.toUpperCase(Locale.ROOT) + "_LOGGER");
        body.insertBegin(snippet);
        CtMethod<?> method = e.getFactory().createMethod(e, modifiers, returnType, "get" + name + "Logger", null, null, body);
        e.addMethod(method);
    }

}
