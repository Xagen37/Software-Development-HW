import aspect.LoggingExecutionTimeAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import tokenizer.*;
import tokenizer.Error;
import tokenizer.Number;
import visitor.CalcVisitor;
import visitor.ParserVisitor;
import visitor.PrintVisitor;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class ContextConfiguration {
    @Bean
    public LoggingExecutionTimeAspect aspect() { return new LoggingExecutionTimeAspect(); }

    @Bean
    public CalcVisitor calcVisitor() { return new CalcVisitor(); }

    @Bean
    public ParserVisitor parserVisitor() { return new ParserVisitor(); }

    @Bean
    public PrintVisitor printVisitor() { return new PrintVisitor(); }

    @Bean
    public Tokenizer tokenizer() { return new Tokenizer(); }

    @Bean
    public End end() { return new End(); }

    @Bean
    public Error error() { return new Error(); }

    @Bean
    public Number number() { return new Number(new Start()); }

    @Bean
    public Start start() { return new Start(); }
}
