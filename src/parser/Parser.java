package parser;

import interpreter.*;

import java.util.ArrayList;
import java.util.List;

/**/
public class Parser {
    private Scanner scan = null;
    private Lexeme lookahead = null;

    public List<Function> subroutines = null;

    public Parser( String text )
    {
        scan = new Scanner(text + "@");
        lookahead = scan.next();
    }

    public void parse()
    {
        subroutines = new ArrayList<>();

        while( lookahead.is(Token.Declare, Token.Function) ) {
            Function subri = null;
            if( lookahead.is(Token.Declare) )
                subri = parseDeclare();
            else if( lookahead.is(Token.Function) )
                subri = parseFunction();
            if( subri != null )
                subroutines.add(subri);
        }
    }

    private Function parseDeclare()
    {
        match(Token.Declare);
        return parseFuncHeader();
    }

    private Function parseFuncHeader()
    {
        match(Token.Function);
        String name = lookahead.value;
        match(Token.Identifier);
        match(Token.LeftParen);
        List<String> params = new ArrayList<>();
        if( lookahead.is(Token.Identifier) ) {
            String varl = lookahead.value;
            match(Token.Identifier);
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

    private Function parseFunction()
    {
        Function subr = parseFuncHeader();
        subr.body = parseStatementList();
        match(Token.End);
        match(Token.Function);

        return subr;
    }

    private Statement parseStatementList()
    {
        Sequence seq = new Sequence();
        while( lookahead.is(Token.Identifier, Token.Input, Token.Print, Token.If, Token.For, Token.While) ) {
            Statement sti = parseStatement();
            seq.add(sti);
        }

        return seq;
    }

    private Statement parseStatement()
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
        }
        parseNewLines();

        return stat;
    }

    private Statement parseAssignment()
    {
        String varl = lookahead.value;
        match(Token.Identifier);
        match(Token.Eq);
        Expression exl = parseDisjunction();

        return new Assignment(varl, exl);
    }

    private Statement parseInput()
    {
        match(Token.Input);
        String varn = lookahead.value;
        match(Token.Identifier);

        return new Input(varn);
    }

    private Statement parsePrint()
    {
        match(Token.Print);
        Expression exo = parseDisjunction();

        return null;
    }

    private Statement parseConditional()
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
            parseNewLines();
            Statement ste = parseStatementList();
            Branch bre = new Branch(coe, ste);
            bi.setElse(bre);
            bi = bre;
        }
        if( lookahead.is(Token.Else) ) {
            match(Token.Else);
            Statement bre = parseStatementList();
            bi.setElse(bre);
        }
        match(Token.End);
        match(Token.If);

        return statbr;
    }

    private Statement parseForLoop()
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

    private Statement parseWhileLoop()
    {
        match(Token.While);
        Expression cond = parseDisjunction();
        parseNewLines();
        Statement bdy = parseStatementList();
        match(Token.End);
        match(Token.While);

        return new WhileLoop(cond, bdy);
    }

    private void parseNewLines()
    {
        match(Token.NewLine);
        while( lookahead.is(Token.NewLine) )
            lookahead = scan.next();
    }

    private Expression parseDisjunction()
    {
        Expression exo = parseConjunction();
        while( lookahead.is(Token.Or) ) {
            lookahead = scan.next();
            Expression exi = parseConjunction();
            return new Binary("OR", exo, exi);
        }
        return exo;
    }

    private Expression parseConjunction()
    {
        Expression exo = parseEquation();
        while( lookahead.is(Token.And) ) {
            lookahead = scan.next();
            Expression exi = parseEquation();
            return new Binary("AND", exo, exi);
        }
        return exo;
    }

    private Expression parseEquation()
    {
        Expression exo = parseRelation();
        if( lookahead.is(Token.Eq, Token.Ne) ) {
            String oper = lookahead.value;
            lookahead = scan.next();
            Expression exi = parseRelation();
            exo = new Binary(oper, exo, exi);
        }
        return exo;
    }

    private Expression parseRelation()
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

    private Expression parseAddition()
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

    private Expression parseMultiplication()
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

    private Expression parsePower()
    {
        Expression exo = parseFactor();
        if( lookahead.is(Token.Power) ) {
            match(Token.Power);
            Expression exi = parsePower();
            exo = new Binary("^", exo, exi);
        }
        return exo;
    }

    private Expression parseFactor()
    {
        if( lookahead.is(Token.Number) ) {
            double numval = Double.valueOf(lookahead.value);
            lookahead = scan.next();
            return new Constant(numval);
        }

        if( lookahead.is(Token.Identifier) ) {
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
                return new FunCall(null, argus);
            }
            return new Variable(varnam);
        }

        if( lookahead.is(Token.Sub) ) {
            String oper = lookahead.value;
            lookahead = scan.next();
            Expression subex = parseFactor();
            return new Unary(oper, subex);
        }

        if( lookahead.is(Token.Not) ) {
            String oper = lookahead.value;
            lookahead = scan.next();
            Expression subex = parseFactor();
            return new Unary(oper, subex);
        }

        if( lookahead.is(Token.LeftParen) ) {
            match(Token.LeftParen);
            Expression expr = parseDisjunction();
            match(Token.RightParen);
            return expr;
        }

        return null;
    }

    private void match( Token exp )
    {
        if( lookahead.is(exp) )
            lookahead = scan.next();
    }
}
