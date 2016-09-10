package parser;

import engine.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**/
public class Parser {
    private Scanner scan = null;
    private Token lookahead = null;

    private List<Function> subroutines = null;
    private Map<String,String> symbols = null;

    public Parser( String text )
    {
        scan = new Scanner(text + "@");
        lookahead = scan.next();
        // բաց թողնել ֆայլի սկզբի դատարկ տողերը
        while( lookahead.is(Kind.NewLine) )
            lookahead = scan.next();
    }

    public List<Function> parse() throws ParseError
    {
        subroutines = new ArrayList<>();
        symbols = new HashMap<>();

        while( lookahead.is(Kind.Declare, Kind.Function) ) {
            Function subri = null;
            if( lookahead.is(Kind.Declare) )
                subri = parseDeclare();
            else if( lookahead.is(Kind.Function) )
                subri = parseFunction();
            if( subri != null )
                addSubroutine(subri);
        }

        return subroutines;
    }

    private void addSubroutine( Function su )
    {
        if( !subroutines.contains(su) )
            subroutines.add(su);
    }

    private Function parseDeclare() throws ParseError
    {
        match(Kind.Declare);
        return parseFuncHeader();
    }

    private Function parseFuncHeader() throws ParseError
    {
        match(Kind.Function);
        String name = lookahead.lexeme;
        match(Kind.Identifier);

        // ստուգել ֆունկցիայի՝ դեռևս սահմանված չլինելը
        for( Function si : subroutines )
            if( si.name.equals(name) && si.body != null )
                throw new ParseError(name + " անունով ֆունկցիան արդեն սահմանված է։");

        match(Kind.LeftParen);
        List<Variable> params = new ArrayList<>();
        if( lookahead.is(Kind.Identifier) ) {
            Variable varl = new Variable(lookahead.lexeme);
            match(Kind.Identifier);
            if( params.contains(varl) )
                throw new ParseError(varl + " անունն արդեն կա պարամետրերի ցուցակում։");
            params.add(varl);
            while( lookahead.is(Kind.Comma) ) {
                match(Kind.Comma);
                varl = new Variable(lookahead.lexeme);
                match(Kind.Identifier);
                params.add(varl);
            }
        }
        match(Kind.RightParen);
        parseNewLines();

        return new Function(name, params);
    }

    private Function parseFunction() throws ParseError
    {
        Function subr = parseFuncHeader();
        addSubroutine(subr);
        final String name = subr.name;
        subr = subroutines.stream()
                .filter(e -> e.name.equals(name))
                .findFirst()
                .get();
        subr.body = parseStatementList();
        match(Kind.End);
        match(Kind.Function);
        parseNewLines();

        return subr;
    }

    private Statement parseStatementList() throws ParseError
    {
        Sequence seq = new Sequence();
        // FIRST(Statement)
        while( lookahead.is(Kind.Identifier, Kind.Input, Kind.Print, Kind.If,
                Kind.For, Kind.While, Kind.Call, Kind.Dim, Kind.Let) ) {
            Statement sti = parseStatement();
            seq.add(sti);
        }

        return seq;
    }

    private Statement parseStatement() throws ParseError
    {
        Statement stat = null;
        switch( lookahead.kind ) {
            case Let:
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
            case Dim:
                stat = parseDim();
                break;
        }
        parseNewLines();

        return stat;
    }

    private Statement parseAssignment() throws ParseError
    {
        if( lookahead.is(Kind.Let) )
            match(Kind.Let);
        String varl = lookahead.lexeme;
        match(Kind.Identifier);
        // երբ  վերագրվում է զանգվածի տարրին
        Expression einx = null;
        if( lookahead.is(Kind.LeftParen) ) {
            match(Kind.LeftParen);
            einx = parseDisjunction();
            match(Kind.RightParen);
        }
        match(Kind.Eq);
        Expression exl = parseDisjunction();

        // TODO նոր անուն ավելացնել symbols-ում
        // TODO ստուգել փոփխականի տիպը
        return new Let(new Variable(varl, einx), exl);
    }

    private Statement parseInput() throws ParseError
    {
        match(Kind.Input);
        String varn = lookahead.lexeme;
        match(Kind.Identifier);

        return new Input(new Variable(varn));
    }

    private Statement parsePrint() throws ParseError
    {
        match(Kind.Print);
        Expression exo = parseDisjunction();

        return new Print(exo);
    }

    private Statement parseConditional() throws ParseError
    {
        match(Kind.If);
        Expression cond = parseDisjunction();
        match(Kind.Then);
        parseNewLines();
        Statement thenp = parseStatementList();
        If statbr = new If(cond, thenp);
        If bi = statbr;
        while( lookahead.is(Kind.ElseIf) ) {
            match(Kind.ElseIf);
            Expression coe = parseDisjunction();
            match(Kind.Then);
            parseNewLines();
            Statement ste = parseStatementList();
            If bre = new If(coe, ste);
            bi.setElse(bre);
            bi = bre;
        }
        if( lookahead.is(Kind.Else) ) {
            match(Kind.Else);
            parseNewLines();
            Statement bre = parseStatementList();
            bi.setElse(bre);
        }
        match(Kind.End);
        match(Kind.If);

        return statbr;
    }

    private Statement parseForLoop() throws ParseError
    {
        match(Kind.For);
        Variable prn = new Variable(lookahead.lexeme);
        match(Kind.Identifier);
        if( prn.isText() )
            throw new ParseError("FOR ցիկլի պարամետրը պետք է լինի թվային։");
        match(Kind.Eq);
        Expression init = parseDisjunction();
        match(Kind.To);
        Expression lim = parseDisjunction();
        Expression ste = null;
        if( lookahead.is(Kind.Step) ) {
            match(Kind.Step);
            ste = parseDisjunction();
        }
        parseNewLines();
        Statement bdy = parseStatementList();
        match(Kind.End);
        match(Kind.For);

        return new For(prn, init, lim, ste, bdy);
    }

    private Statement parseWhileLoop() throws ParseError
    {
        match(Kind.While);
        Expression cond = parseDisjunction();
        parseNewLines();
        Statement bdy = parseStatementList();
        match(Kind.End);
        match(Kind.While);

        return new While(cond, bdy);
    }

    private Statement parseCallSub() throws ParseError
    {
        match(Kind.Call);
        String subnam = lookahead.lexeme;
        match(Kind.Identifier);
        // TODO ստուգել ֆունկցիայի սահմանված կամ հայտարարված լինելը
        ArrayList<Expression> argus = new ArrayList<>();
        if( lookahead.is(Kind.Number, Kind.Identifier, Kind.Sub, Kind.Not, Kind.LeftParen) ) {
            Expression exi = parseDisjunction();
            argus.add(exi);
            while( lookahead.is(Kind.Comma) ) {
                lookahead = scan.next();
                exi = parseDisjunction();
                argus.add(exi);
            }
        }

        Function func = subroutines.stream()
                .filter(e -> e.name.equals(subnam))
                .findFirst().get();

        if( func.parameters.size() != argus.size() )
            throw new ParseError("%s ֆունկցիան սպասում է %d պարամետրեր։",
                    subnam, func.parameters.size());

        return new Call(func, argus);
    }

    private Statement parseDim() throws ParseError
    {
        match(Kind.Dim);
        String name = lookahead.lexeme;
        match(Kind.Identifier);
        match(Kind.LeftParen);
        String strsz = lookahead.lexeme;
        match(Kind.Number);
        match(Kind.RightParen);

        Variable var = new Variable(name);
        double size = Double.valueOf(strsz);
        symbols.put(name, "ARRAY"); // ?
        return new Dim(var, (int)size);
    }

    private void parseNewLines() throws ParseError
    {
        match(Kind.NewLine);
        while( lookahead.is(Kind.NewLine) )
            lookahead = scan.next();
    }

    private Expression parseDisjunction() throws ParseError
    {
        Expression exo = parseConjunction();
        while( lookahead.is(Kind.Or) ) {
            lookahead = scan.next();
            Expression exi = parseConjunction();
            return new Binary("OR", exo, exi);
        }
        return exo;
    }

    private Expression parseConjunction() throws ParseError
    {
        Expression exo = parseEquality();
        while( lookahead.is(Kind.And) ) {
            lookahead = scan.next();
            Expression exi = parseEquality();
            return new Binary("AND", exo, exi);
        }
        return exo;
    }

    private Expression parseEquality() throws ParseError
    {
        Expression exo = parseComparison();
        if( lookahead.is(Kind.Eq, Kind.Ne) ) {
            String oper = lookahead.lexeme;
            lookahead = scan.next();
            Expression exi = parseComparison();
            exo = new Binary(oper, exo, exi);
        }
        return exo;
    }

    private Expression parseComparison() throws ParseError
    {
        Expression exo = parseAddition();
        if( lookahead.is(Kind.Gt, Kind.Ge, Kind.Lt, Kind.Le) ) {
            String oper = lookahead.lexeme;
            lookahead = scan.next();
            Expression exi = parseAddition();
            exo = new Binary(oper, exo, exi);
        }
        return exo;
    }

    private Expression parseAddition() throws ParseError
    {
        Expression exo = parseMultiplication();
        while( lookahead.is(Kind.Add, Kind.Sub, Kind.Ampersand) ) {
            String oper = lookahead.lexeme;
            lookahead = scan.next();
            Expression exi = parseMultiplication();
            exo = new Binary(oper, exo, exi);
        }
        return exo;
    }

    private Expression parseMultiplication() throws ParseError
    {
        Expression exo = parsePower();
        while( lookahead.is(Kind.Mul, Kind.Div) ) {
            String oper = lookahead.lexeme;
            lookahead = scan.next();
            Expression exi = parsePower();
            exo = new Binary(oper, exo, exi);
        }
        return exo;
    }

    private Expression parsePower() throws ParseError
    {
        Expression exo = parseFactor();
        if( lookahead.is(Kind.Power) ) {
            match(Kind.Power);
            Expression exi = parsePower();
            exo = new Binary("^", exo, exi);
        }
        return exo;
    }

    private Expression parseFactor() throws ParseError
    {
        Expression result = null;
        if( lookahead.is(Kind.Number) ) {
            double numval = Double.valueOf(lookahead.lexeme);
            lookahead = scan.next();
            result = new Real(numval);
        }
        else if( lookahead.is(Kind.Text) ) {
            String textval = lookahead.lexeme;
            lookahead = scan.next();
            result = new Text(textval);
        }
        else if( lookahead.is(Kind.Identifier) ) {
            String varnam = lookahead.lexeme;
            lookahead = scan.next();
            if( lookahead.is(Kind.LeftParen) ) {
                ArrayList<Expression> argus = new ArrayList<>();
                match(Kind.LeftParen);
                // FIRST(Disjunction)
                if( lookahead.is(Kind.Number, Kind.Identifier, Kind.Sub, Kind.Not, Kind.LeftParen) ) {
                    Expression exi = parseDisjunction();
                    argus.add(exi);
                    while( lookahead.is(Kind.Comma) ) {
                        lookahead = scan.next();
                        exi = parseDisjunction();
                        argus.add(exi);
                    }
                }
                match(Kind.RightParen);
                // ստուգել ներդրված ֆունկցիա լինելը
                if( BuiltIn.isInternal(varnam) )
                    return new BuiltIn(varnam, argus);
                // գտնել հայտարարված կամ սահմանված ֆունկցիան
                Function func = subroutines.stream()
                        .filter(e -> e.name.equals(varnam))
                        .findFirst().get();
                // համեմատել ֆունկցիայի պարամետրերի և փոխանցված արգումենտների քանակը
                if( func.parameters.size() != argus.size() )
                    throw new ParseError("%s ֆունկցիան սպասում է %d պարամետրեր։",
                            varnam, func.parameters.size());
                result = new Apply(func, argus);
            }
            else
                result = new Variable(varnam);
        }
        else if( lookahead.is(Kind.Sub, Kind.Not) ) {
            String oper = lookahead.lexeme;
            lookahead = scan.next();
            Expression subex = parseFactor();
            result = new Unary(oper, subex);
        }
        else if( lookahead.is(Kind.LeftParen) ) {
            match(Kind.LeftParen);
            result = parseDisjunction();
            match(Kind.RightParen);
        }

        return result;
    }

    private void match( Kind exp ) throws ParseError
    {
        if( lookahead.is(exp) )
            lookahead = scan.next();
        else
            throw new ParseError("Շարահյուսական սխալ։ %d տողում սպասվում էր %s, բայց հանդիպել է %s",
                    lookahead.line, exp, lookahead.kind);
    }
}
