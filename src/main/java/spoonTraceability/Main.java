package spoonTraceability;

import spoon.Launcher;
import spoonTraceability.processors.InitSpoon;
import spoonTraceability.processors.LogSpoon;
import spoonTraceability.processors.MainSpoon;

public class Main {
    //Questions 3, 4 et 5
    public static void main (String[] args) {
        Launcher spoon = new Launcher();
        MainSpoon mainSpoon = new MainSpoon();
        InitSpoon initSpoon = new InitSpoon();
        LogSpoon logSpoon = new LogSpoon();

        spoon.addInputResource("./src/main/java/OutPut");
        spoon.setSourceOutputDirectory("./src/test/java");
        spoon.getEnvironment().setSourceClasspath(new String[] { "./src/test/java" });
        spoon.getEnvironment().setAutoImports(true);
        spoon.addProcessor( mainSpoon );
        spoon.addProcessor(initSpoon);
        spoon.addProcessor(logSpoon);
        spoon.run();
        System.out.println("La génération de code pour la traçabilité a été effectuée dans /src/test/java/.");

        
    }
}
