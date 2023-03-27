import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import tokenizer.State;
import tokenizer.Tokenizer;
import token.Token;
import visitor.CalcVisitor;
import visitor.ParserVisitor;
import visitor.PrintVisitor;
import visitor.TokenVisitor;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(ContextConfiguration.class);

        ParserVisitor parser = (ParserVisitor) ctx.getBean(ParserVisitor.getName());
        PrintVisitor print = (PrintVisitor) ctx.getBean(PrintVisitor.getName());
        CalcVisitor calc = (CalcVisitor) ctx.getBean(CalcVisitor.getName());
        Tokenizer tokenizer = ctx.getBean(Tokenizer.class);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome, user. Print expressions until you are tired.");
        System.out.println("Remember to put spaces between each number, operation or bracket.");
        do {
            try {
                String expr = scanner.nextLine();

                List<Token> tokens = tokenizer.parse(expr);
                tokens.forEach(tok -> tok.accept(parser));

                tokens = parser.get();
                tokens.forEach(tok -> tok.accept(print));
                String printed = print.get();

                tokens.forEach(tok -> tok.accept(calc));
                int result = calc.get();

                System.out.println("Your expression: " + printed);
                System.out.println("Your result: " + result);

                parser.flush();
                print.flush();
                calc.flush();
            } catch (Exception e) {
                System.out.println("You did something wrong, user!");
                e.printStackTrace();
            }
        } while (scanner.hasNext());
    }
}
