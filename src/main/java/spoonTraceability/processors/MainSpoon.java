package spoonTraceability.processors;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtCodeSnippetStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtExecutable;

public class MainSpoon extends AbstractProcessor<CtExecutable<?>> {

    @Override
    public boolean isToBeProcessed (CtExecutable e) {
        CtClass<?> classe = e.getParent(CtClass.class);

        return classe != null && classe.getQualifiedName().equals("OutPut.Main");
    }

    @Override
    public void process (CtExecutable e) {
        if(e.getBody() == null || !e.getSimpleName().equals("createLoggers"))
            return;

        CtCodeSnippetStatement snippet = getFactory().Core().createCodeSnippetStatement();
        snippet.setValue("try{\n" +
                "            Formatter formatter = new Formatter();\n" +
                "            java.util.logging.Handler readFH = new java.util.logging.FileHandler(\"./readlog.log\");\n" +
                "            java.util.logging.Handler writeFH = new java.util.logging.FileHandler(\"./writes.log\");\n" +
                "            java.util.logging.Logger readLogger = Repository.getReadLogger();\n" +
                "            java.util.logging.Logger writeLogger = Repository.getWriteLogger();\n" +
                "\n" +
                "            readFH.setFormatter(formatter);\n" +
                "            readFH.setLevel(java.util.logging.Level.ALL);\n" +
                "            readLogger.setLevel(java.util.logging.Level.ALL);\n" +
                "            readLogger.addHandler(readFH);\n" +
                "\n" +
                "            writeFH.setFormatter(formatter);\n" +
                "            writeFH.setLevel(java.util.logging.Level.ALL);\n" +
                "            writeLogger.setLevel(java.util.logging.Level.ALL);\n" +
                "            writeLogger.addHandler(writeFH);\n" +
                "        }catch (Exception e){\n" +
                "            System.err.println(\"Impossible d'initialiser les loggers...\");\n" +
                "            e.printStackTrace();\n" +
                "            System.exit(1);\n" +
                "        }");

        e.getBody().insertBegin(snippet);
    }
}
