package server.fix.hnx;

import java.io.File;

import org.quickfixj.codegenerator.MessageCodeGenerator;
import org.quickfixj.codegenerator.MessageCodeGenerator.Task;

public class Customizing {
    public static void main(String[] args) {
    
        String basePackage = "test";
        Task task = new Task();
        task.setName("Custom HNX");
        task.setSpecification(new File("src/main/resources/FIX42.xml"));
        task.setTransformDirectory(new File("src/main/resources/transform12313213"));
        task.setOutputBaseDirectory(new File("src/main/resources/output-src32131321321"));
        task.setOverwrite(true);
        task.setMessagePackage(basePackage + ".messages");
        task.setFieldPackage(basePackage + ".fields");
        MessageCodeGenerator codeGenerator = new MessageCodeGenerator();
        codeGenerator.generate(task);
    }
}
