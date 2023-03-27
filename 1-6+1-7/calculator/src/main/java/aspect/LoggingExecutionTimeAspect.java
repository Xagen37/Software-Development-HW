package aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import tokenizer.*;
import tokenizer.Error;
import tokenizer.Number;
import visitor.CalcVisitor;
import visitor.ParserVisitor;
import visitor.PrintVisitor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Aspect
public class LoggingExecutionTimeAspect {
    public static Set<String> logs = Set.of(
            Tokenizer.getName(),
            End.getName(),
            Error.getName(),
            Number.getName(),
            Start.getName(),
            PrintVisitor.getName(),
            ParserVisitor.getName(),
            CalcVisitor.getName()
    );

    public static Set<String> timeStat = Set.of(
            PrintVisitor.getName(),
            ParserVisitor.getName(),
            CalcVisitor.getName()
    );

    private int tabulation = 0;
    private final Map<String, Long> visitCounter = new HashMap<>();
    private final Map<String, Long> timeCounter  = new HashMap<>();

    public Object logExecutionTime(ProceedingJoinPoint joinPoint, String name) throws Throwable {
        final long startTime = System.nanoTime();
        if (logs.contains(name)) {
            System.err.println("-".repeat(tabulation) +
                    "Start method " +
                    joinPoint.getSignature().getName() +
                    " of " + name +
                    " with args: " + Arrays.toString(joinPoint.getArgs()));
            tabulation++;
        }

        final Object result = joinPoint.proceed(joinPoint.getArgs());

        final long elapsedTime = System.nanoTime() - startTime;
        if (logs.contains(name)) {
            tabulation--;
            System.err.println("-".repeat(tabulation) + "Finish method " + joinPoint.getSignature().getName()
                    + ", execution time in ns: " + elapsedTime);
        }

        if (timeStat.contains(name)) {
            if (!visitCounter.containsKey(name)) {
                visitCounter.put(name, 0L);
                timeCounter.put(name, 0L);
            }
            assert timeCounter.containsKey(name);
            visitCounter.put(name, visitCounter.get(name) + 1);
            timeCounter.put(name, timeCounter.get(name) + elapsedTime);
        }

        return result;
    }

    @Around("execution(* tokenizer.Tokenizer.parse(..))")
    public Object logExecutionTimeTokenizer(ProceedingJoinPoint joinPoint) throws Throwable {
        return logExecutionTime(joinPoint, Tokenizer.getName());
    }

    @Around("execution(* tokenizer.End.getToken(..))")
    public Object logExecutionTimeEnd(ProceedingJoinPoint joinPoint) throws Throwable {
        return logExecutionTime(joinPoint, End.getName());
    }

    @Around("execution(* tokenizer.Error.getToken(..))")
    public Object logExecutionTimeError(ProceedingJoinPoint joinPoint) throws Throwable {
        return logExecutionTime(joinPoint, Error.getName());
    }

    @Around("execution(* tokenizer.Number.getToken(..))")
    public Object logExecutionTimeNumber(ProceedingJoinPoint joinPoint) throws Throwable {
        return logExecutionTime(joinPoint, Number.getName());
    }

    @Around("execution(* tokenizer.Start.getToken(..))")
    public Object logExecutionTimeStart(ProceedingJoinPoint joinPoint) throws Throwable {
        return logExecutionTime(joinPoint, Start.getName());
    }

    @Around("execution(* visitor.ParserVisitor.*(..))")
    public Object logExecutionTimeParserVisitor(ProceedingJoinPoint joinPoint) throws Throwable {
        return logExecutionTime(joinPoint, ParserVisitor.getName());
    }

    @Around("execution(* visitor.PrintVisitor.*(..))")
    public Object logExecutionTimePrintVisitor(ProceedingJoinPoint joinPoint) throws Throwable {
        return logExecutionTime(joinPoint, PrintVisitor.getName());
    }

    @Around("execution(* visitor.CalcVisitor.*(..))")
    public Object logExecutionTimeCalcVisitor(ProceedingJoinPoint joinPoint) throws Throwable {
        return logExecutionTime(joinPoint, CalcVisitor.getName());
    }

    @AfterReturning("execution(* visitor.CalcVisitor.get(..))")
    public void showStat() {
        if (!timeStat.isEmpty()) {
            System.err.println("\nTimes called:");
            visitCounter.forEach((name, time) -> System.err.println(name + ":\t" + time));

            System.err.println("\nSummary execution time, ns:");
            timeCounter.forEach((name, time) -> System.err.println(name + ":\t" + time));

            System.err.println("\nAverage execution time, ns:");
            timeCounter.forEach((name, time) -> System.err.println(name + ":\t" + (time / visitCounter.get(name))));
        }
    }
}
