package logging;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtCodeSnippetStatement;
import spoon.reflect.declaration.CtExecutable;

public class LogProcessor extends AbstractProcessor<CtExecutable<?>> {

    @Override
    public void process(CtExecutable e) {
        CtCodeSnippetStatement snippet = getFactory().Core().createCodeSnippetStatement();

          // Extrait contenant le journal. 
       snippet.setValue("LOGGER.log(Level.FINE, \"Error occur in FileHandler.\", e);");

        // Insère l'extrait de code au début du corps de la méthode.
        if (e.getBody() != null) {
            e.getBody().insertBegin(snippet);
        }
    }
}
