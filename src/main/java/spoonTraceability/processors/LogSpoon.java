package spoonTraceability.processors;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtCodeSnippetStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtExecutable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LogSpoon extends AbstractProcessor<CtExecutable<?>> {

   @Override
    public boolean isToBeProcessed(CtExecutable e) {
       CtClass<?> classe = e.getParent(CtClass.class);

       return classe != null && classe.getQualifiedName().equals("OutPut.Repository");
   }

    @Override
    public void process(CtExecutable e) {
        if(e.getBody() == null)
            return;

        String methodName = e.getSimpleName();
        // Extrait contenant de log.
        CtCodeSnippetStatement snippet = getFactory().Core().createCodeSnippetStatement();
        final String event;

        // Insère l'extrait de code au début du corps de la méthode
        switch(methodName) {
            case "affichege":
            case "chercher":
                event = "Read"; break;
            case "ajout":
            case "update":
            case "supprimer":
                event = "Write"; break;
            default:
                event = null;
        }

        if(event == null)
            return;

        snippet.setValue(log(event, methodName));
        e.getBody().insertEnd(snippet);
    }

    private String log(String event, String action) {
       String template = "Repository.get%sLogger().log(java.util.logging.Level.FINER, \"[%s - %s] User \" + user + \" a fait une opération de %s";
       DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
       LocalDateTime now = LocalDateTime.now();
       template += action.equals("affichage") ? "\")" : " sur le produit \" + product)";

       return String.format(template, event, dtf.format(now), event.toUpperCase(Locale.ROOT), action);
    }
}
