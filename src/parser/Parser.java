package parser;

import engine.*;
import engine.Function;

import java.util.ArrayList;
import java.util.List;
import java.util.function.*;

/**/
public class Parser {
    private Scanner scan = null;
    private Lexeme lookahead = null;

    private List<Function> subroutines = null;

    public Parser( String text )
    {
        scan = new Scanner(text + "@");
        lookahead = scan.next();
        // բաց թողնել ֆայլի սկզբի դատարկ տողերը
        while( lookahead.is(Token.NewLine) )
            lookahead = scan.next();
    }

    public List<Function> parse() throws SyntaxError
    {
        subroutines = new ArrayList<>();

        while( lookahead.is(Token.Declare, Token.Function) ) {
            Function subri = null;
            if( lookahead.is(Token.Declare) )
                subri = parseDeclare();
            else if( lookahead.is(Token.Function) )
                subri = parseFunction();
            addSubroutine(subri);
        }

        return subroutines;
    }

    private void addSubroutine( Function su )
    {
        if( su != null && !subroutines.contains(su) )
            subroutines.add(su);
    }

    private Function parseDeclare() throws SyntaxError
    {
        match(Token.Declare);
        return parseFuncHeader();
    }

    private Function parseFuncHeader() throws SyntaxError
    {
        match(Token.Function);
        String name = lookahead.value;
        match(Token.Identifier);
        // TODO ստուգել ֆունկցիայի՝ դեռևս սահմանված չլինելը
//        for( Function si : subroutines )
//            if( si.name.equals(name) )
//                throw new SyntaxError(name + " անունով ֆունկցիան արդեն սահմանված է։");
        match(Token.LeftParen);
        List<String> params = new ArrayList<>();
        if( lookahead.is(Token.Identifier) ) {
            String varl = lookahead.value;
            match(Token.Identifier);
            if( params.contains(varl) )
                throw new SyntaxError(varl + " անունն արդեն կա պարամետրերի ցուցակում։");
            params.add(varl);
            while( lookahead.is(Token.Comma) ) {
                match(Token.Comma);
                varl = lookahead.value;
                match(Token.Identifier);
                params.add(varl);
            }
        }
        match(Token.RightParen);
        parseNewLines();

        return new Function(name, params, null);
    }

    private Function parseFunction() throws SyntaxError
    {
        Function subr = parseFuncHeader();
        addSubroutine(subr);
        final String name = subr.name;
        subr = subroutines.stream()
                .filter(e -> e.name.equals(name))
                .findFirst().get();
        subr.body = parseStatementList();
        match(Token.End);
        match(Token.Function);
        parseNewLines();

        return subr;
    }

    private Statement parseStatementList() throws SyntaxError
    {
        Sequence seq = new Sequence();
        while( lookahead.is(Token.Identifier, Token.Input, Token.Print, Token.If, Token.For, Token.While, Token.Call) ) {
            Statement sti = parseStatement();
            seq.add(sti);
        }

        return seq;
    }

    private Statement parseStatement() throws SyntaxError
    {
        Statement stat = null;
        switch( lookahead.kind ) {
            case Identifier:
                stat = parseAssignment();
                break;
            case Input:
                stat = parseInput();
                break;
            case Print:
                stat = parsePrint();
                break;
            case If:
                stat = parseConditional();
                break;
            case For:
                stat = parseForLoop();
                break;
            case While:
                stat = parseWhileLoop();
                break;
            case Call:
                stat = parseCallSub();
                break;
        }
        parseNewLines();

        return stat;
    }

    private Statement parseAssignment() throws SyntaxError
    {
        String varl = lookahead.value;
        match(Token.Identifier);
        match(Token.Eq);
        Expression exl = parseDisjunction();

        return new Assignment(varl, exl);
    }

    private Statement parseInput() throws SyntaxError
    {
        match(Token.Input);
        String varn = lookahead.value;
        match(Token.Identifier);

        return new Input(varn);
    }

    private Statement parsePrint() throws SyntaxError
    {
        match(Token.Print);
        Expression exo = parseDisjunction();

        return new Print(exo);
    }

    private Statement parseConditional() throws SyntaxError
    {
        match(Token.If);
        Expression cond = parseDisjunction();
        match(Token.Then);
        parseNewLines();
        Statement thenp = parseStatementList();
        Branch statbr = new Branch(cond, thenp);
        Branch bi = statbr;
        while( lookahead.is(Token.ElseIf) ) {
            match(Token.ElseIf);
            Expression coe = parseDisjunction();
            match(Token.Then);
            parseNewLines();
            Statement ste = parseStatementList();
            Branch bre = new Branch(coe, ste);
            bi.setElse(bre);
            bi = bre;
        }
        if( lookahead.is(Token.Else) ) {
            match(Token.Else);
            parseNewLines();
            Statement bre = parseStatementList();
            bi.setElse(bre);
        }
        match(Token.End);
        match(Token.If);

        return statbr;
    }

    private Statement parseForLoop() throws SyntaxError
    {
        match(Token.For);
        String prn = lookahead.value;
        match(Token.Identifier);
        match(Token.Eq);
        Expression init = parseDisjunction(); // Addition ?
        match(Token.To);
        Expression lim = parseDisjunction(); // Addition ?
        Expression ste = null;
        if( lookahead.is(Token.Step) ) {
            match(Token.Step);
            ste = parseDisjunction(); // Addition ?
        }
        parseNewLines();
        Statement bdy = parseStatementList();
        match(Token.End);
        match(Token.For);

        return new ForLoop(prn, init, lim, ste, bdy);
    }

    private Statement parseWhileLoop() throws SyntaxError
    {
        match(Token.While);
        Expression cond = parseDisjunction();
        parseNewLines();
        Statement bdy = parseStatementList();
        match(Token.End);
        match(Token.While);

        return new WhileLoop(cond, bdy);
    }

    private Statement parseCallSub() throws SyntaxError
    {
        match(Token.Call);
        String subnam = lookahead.value;
        match(Token.Identifier);
        // TODO ստուգել ֆունկցիայի սահմանված կամ հայտարարված լինելը
        ArrayList<Expression> argus = new ArrayList<>();
        if( lookahead.is(Token.Number, Token.Identifier, Token.Sub, Token.Not, Token.LeftParen) ) {
            Expression exi = parseDisjunction();
            argus.add(exi);
            while( lookahead.is(Token.Comma) ) {
                lookahead = scan.next();
                exi = parseDisjunction();
                argus.add(exi);
            }
        }

        Function func = subroutines.stream()
                .filter(e -> e.name.equals(subnam))
                .findFirst().get();

        if( func.parameters.size() != argus.size() )
            throw new SyntaxError(String.format("%s ֆունկցիան սպասում է %d պարամետրեր։",
                    subnam, func.parameters.size()));

        return new CallSubr(func, argus);
    }

    private void parseNewLines() throws SyntaxError
    {
        match(Token.NewLine);
        while( lookahead.is(Token.NewLine) )
            lookahead = scan.next();
    }

    private Expression parseDisjunction() throws SyntaxError
    {
        Expression exo = parseConjunction();
        while( lookahead.is(Token.Or) ) {
            lookahead = scan.next();
            Expression exi = parseConjunction();
            return new Binary("OR", exo, exi);
        }
        return exo;
    }

    private Expression parseConjunction() throws SyntaxError
    {
        Expression exo = parseEquality();
        while( lookahead.is(Token.And) ) {
            lookahead = scan.next();
            Expression exi = parseEquality();
            return new Binary("AND", exo, exi);
        }
        return exo;
    }

    private Expression parseEquality() throws SyntaxError
    {
        Expression exo = parseComparison();
        if( lookahead.is(Token.Eq, Token.Ne) ) {
            String oper = lookahead.value;
            lookahead = scan.next();
            Expression exi = parseComparison();
            exo = new Binary(oper, exo, exi);
        }
        return exo;
    }

    private Expression parseComparison() throws SyntaxError
    {
        Expression exo = parseAddition();
        if( lookahead.is(Token.Gt, Token.Ge, Token.Lt, Token.Le) ) {
            String oper = lookahead.value;
            lookahead = scan.next();
            Expression exi = parseAddition();
            exo = new Binary(oper, exo, exi);
        }
        return exo;
    }

    private Expression parseAddition() throws SyntaxError
    {
        Expression exo = parseMultiplication();
        while( lookahead.is(Token.Add, Token.Sub) ) {
            String oper = lookahead.value;
            lookahead = scan.next();
            Expression exi = parseMultiplication();
            exo = new Binary(oper, exo, exi);
        }
        return exo;
    }

    private Expression parseMultiplication() throws SyntaxError
    {
        Expression exo = parsePower();
        while( lookahead.is(Token.Mul, Token.Div) ) {
            String oper = lookahead.value;
            lookahead = scan.next();
            Expression exi = parsePower();
            exo = new Binary(oper, exo, exi);
        }
        return exo;
    }

    private Expression parsePower() throws SyntaxError
    {
        Expression exo = parseFactor();
        if( lookahead.is(Token.Power) ) {
            match(Token.Power);
            Expression exi = parsePower();
            exo = new Binary("^", exo, exi);
        }
        return exo;
    }

    private Expression parseFactor() throws SyntaxError
    {
        Expression result = null;
        if( lookahead.is(Token.Number) ) {
            double numval = Double.valueOf(lookahead.value);
            lookahead = scan.next();
            result = new Constant(numval);
        }
        else if( lookahead.is(Token.Identifier) ) {
            String varnam = lookahead.value;
            lookahead = scan.next();
            if( lookahead.is(Token.LeftParen) ) {
                ArrayList<Expression> argus = new ArrayList<>();
                match(Token.LeftParen);
                // FIRST(Disjunction)
                if( lookahead.is(Token.Number, Token.Identifier, Token.Sub, Token.Not, Token.LeftParen) ) {
                    Expression exi = parseDisjunction();
                    argus.add(exi);
                    while( lookahead.is(Token.Comma) ) {
                        lookahead = scan.next();
                        exi = parseDisjunction();
                        argus.add(exi);
                    }
                }
                match(Token.RightParen);
                // ստուգել ներդրված ֆունկցիա լինելը
                if( Internal.isInternal(varnam) )
                    return new Internal(varnam, argus);
                // գտնել հայտարարված կամ սահմանված ֆունկցիան
                Function func = subroutines.stream()
                        .filter(e -> e.name.equals(varnam))
                        .findFirst().get();
                // համեմատել ֆունկցիայի պարամետրերի և փոխանցված արգումենտների քանակը
                if( func.parameters.size() != argus.size() )
                    throw new SyntaxError(String.format("%s ֆունկցիան սպասում է %d պարամետրեր։",
                            varnam, func.parameters.size()));
                result = new ApplyFunc(func, argus);
            }
            else
                result = new Variable(varnam);
        }
        else if( lookahead.is(Token.Sub, Token.Not) ) {
            String oper = lookahead.value;
            lookahead = scan.next();
            Expression subex = parseFactor();
            result = new Unary(oper, subex);
        }
        else if( lookahead.is(Token.LeftParen) ) {
            match(Token.LeftParen);
            result = parseDisjunction();
            match(Token.RightParen);
        }

        return result;
    }

    private void match( Token exp ) throws SyntaxError
    {
        if( lookahead.is(exp) )
            lookahead = scan.next();
        else {
            String message = String.format("Շարահյուսական սխալ։ %d տողում սպասվում էր %s, բայց հանդիպել է %s",
                    lookahead.line, exp, lookahead.kind);
            throw new SyntaxError(message);
        }
    }
}
