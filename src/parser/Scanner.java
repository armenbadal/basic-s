package parser;

import java.util.HashMap;
import java.util.Map;

/**/
public class Scanner {
    private static Map<String,Token> keywords = null;
    static {
        keywords = new HashMap<>();
        keywords.put("DECLARE", Token.Declare);
        keywords.put("FUNCTION", Token.Function);
        keywords.put("END", Token.End);
        keywords.put("DIM", Token.Dim);
        keywords.put("LET", Token.Let);
        keywords.put("INPUT", Token.Input);
        keywords.put("PRINT", Token.Print);
        keywords.put("IF", Token.If);
        keywords.put("THEN", Token.Then);
        keywords.put("ELSEIF", Token.ElseIf);
        keywords.put("ELSE", Token.Else);
        keywords.put("FOR", Token.For);
        keywords.put("TO", Token.To);
        keywords.put("STEP", Token.Step);
        keywords.put("WHILE", Token.While);
        keywords.put("CALL", Token.Call);
        keywords.put("AND", Token.And);
        keywords.put("OR", Token.Or);
        keywords.put("NOT", Token.Not);
    }

    private char[] source = null;
    private int position = 0;

    public int line = 1;

    //
    public Scanner( String text )
    {
        source = text.toCharArray();
    }

    //
    public Lexeme next()
    {
        char ch = source[position++];

        // անտեսել բացատները
        while( ch == ' ' || ch == '\t' )
            ch = source[position++];

        // հոսքի ավարտ
        if( position == source.length )
            return new Lexeme(Token.Eos, line);

        // մեկնաբանություն
        if( ch == '\'' ) {
            do
                ch = source[position++];
            while( ch != '\n' );
            --position;

            return next();
        }

        // ծառայողական բառեր և իդենտիֆիկատոր
        if( Character.isLetter(ch) )
            return keywordOrIdentifier();

        // թվային լիտերալ
        if( Character.isDigit(ch) )
            return numericLiteral();

        // տողային լիտերալ
        if( ch == '"' )
            return textLiteral();

        // մետասիմվոլներ կամ գործողություններ
        if( ch == '\n' )
            return new Lexeme(Token.NewLine, line++);

        // >, >=
        if( ch == '>' ) {
            ch = source[position++];
            if( ch == '=' )
                return new Lexeme(Token.Ge, ">=", line);
            else
                --position;
            return new Lexeme(Token.Gt, ">", line);
        }

        // <, <=, <>
        if( ch == '<' ) {
            ch = source[position++];
            if( ch == '=' )
                return new Lexeme(Token.Le, "<=", line);
            else if( ch == '>' )
                return new Lexeme(Token.Ne, "<>", line);
            else
                --position;
            return new Lexeme(Token.Lt, "<", line);
        }

        Token kind = Token.Unknown;
        switch( ch ) {
            case '=':
                kind = Token.Eq;
                break;
            case '+':
                kind = Token.Add;
                break;
            case '-':
                kind = Token.Sub;
                break;
            case '*':
                kind = Token.Mul;
                break;
            case '/':
                kind = Token.Div;
                break;
            case '^':
                kind = Token.Power;
                break;
            case '(':
                kind = Token.LeftParen;
                break;
            case ')':
                kind = Token.RightParen;
                break;
            case ',':
                kind = Token.Comma;
                break;
        }

        return new Lexeme(kind, String.valueOf(ch), line);
    }

    //
    private Lexeme keywordOrIdentifier()
    {
        int begin = position - 1;
        char ch = source[begin];
        while( Character.isLetterOrDigit(ch) )
            ch = source[position++];
        if( ch != '$' )
            --position;
        String vl = String.copyValueOf(source, begin, position - begin);
        Token kd = keywords.getOrDefault(vl, Token.Identifier);
        return new Lexeme(kd, vl, line);
    }

    //
    private Lexeme numericLiteral()
    {
        int begin = position - 1;
        char ch = source[begin];
        while( Character.isDigit(ch) )
            ch = source[position++];
        if( ch == '.' ) {
            ch = source[position++];
            while( Character.isDigit(ch) )
                ch = source[position++];
        }
        --position;
        String vl = String.copyValueOf(source, begin, position - begin);
        return new Lexeme(Token.Number, vl, line);
    }

    private Lexeme textLiteral()
    {
        int begin = position;
        char ch = source[begin];
        while( ch != '"' )
            ch = source[position++];
        String vl = String.copyValueOf(source, begin, position - begin);
        return new Lexeme(Token.Text, vl, line);
    }
}
