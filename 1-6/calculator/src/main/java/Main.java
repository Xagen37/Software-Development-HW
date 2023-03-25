import tokenizer.Tokenizer;
import token.Token;
import visitor.CalcVisitor;
import visitor.ParserVisitor;
import visitor.PrintVisitor;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome, user. Print expressions until you are tired.");
        System.out.println("Remember to put spaces between each number, operation or bracket.");
        do {
            try {
                String expr = scanner.nextLine();

                List<Token> tokens = Tokenizer.parse(expr);
                ParserVisitor parser = new ParserVisitor();
                tokens.forEach(tok -> tok.accept(parser));

                tokens = parser.get();
                PrintVisitor print = new PrintVisitor();
                tokens.forEach(tok -> tok.accept(print));
                String printed = print.get();

                CalcVisitor calc = new CalcVisitor();
                tokens.forEach(tok -> tok.accept(calc));
                int result = calc.get();

                System.out.println("Your expression: " + printed);
                System.out.println("Your result: " + result);
            } catch (Exception e) {
                System.out.println("You did something wrong, user!");
                e.printStackTrace();
            }
        } while (scanner.hasNext());
    }
}
