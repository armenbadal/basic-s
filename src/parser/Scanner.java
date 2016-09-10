package parser;

import java.util.HashMap;
import java.util.Map;

/**/
public class Scanner {
    private static Map<String, Kind> keywords = null;
    static {
        keywords = new HashMap<>();
        keywords.put("DECLARE", Kind.Declare);
        keywords.put("FUNCTION", Kind.Function);
        keywords.put("END", Kind.End);
        keywords.put("DIM", Kind.Dim);
        keywords.put("LET", Kind.Let);
        keywords.put("INPUT", Kind.Input);
        keywords.put("PRINT", Kind.Print);
        keywords.put("IF", Kind.If);
        keywords.put("THEN", Kind.Then);
        keywords.put("ELSEIF", Kind.ElseIf);
        keywords.put("ELSE", Kind.Else);
        keywords.put("FOR", Kind.For);
        keywords.put("TO", Kind.To);
        keywords.put("STEP", Kind.Step);
        keywords.put("WHILE", Kind.While);
        keywords.put("CALL", Kind.Call);
        keywords.put("AND", Kind.And);
        keywords.put("OR", Kind.Or);
        keywords.put("NOT", Kind.Not);
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
    public Token next()
    {
        char ch = source[position++];

        // անտեսել բացատները
        while( ch == ' ' || ch == '\t' )
            ch = source[position++];

        // հոսքի ավարտ
        if( position == source.length )
            return new Token(Kind.Eos, line);

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
            return new Token(Kind.NewLine, line++);

        // >, >=
        if( ch == '>' ) {
            ch = source[position++];
            if( ch == '=' )
                return new Token(Kind.Ge, ">=", line);
            else
                --position;
            return new Token(Kind.Gt, ">", line);
        }

        // <, <=, <>
        if( ch == '<' ) {
            ch = source[position++];
            if( ch == '=' )
                return new Token(Kind.Le, "<=", line);
            else if( ch == '>' )
                return new Token(Kind.Ne, "<>", line);
            else
                --position;
            return new Token(Kind.Lt, "<", line);
        }

        Kind kind = Kind.Unknown;
        switch( ch ) {
            case '=':
                kind = Kind.Eq;
                break;
            case '+':
                kind = Kind.Add;
                break;
            case '-':
                kind = Kind.Sub;
                break;
            case '*':
                kind = Kind.Mul;
                break;
            case '/':
                kind = Kind.Div;
                break;
            case '^':
                kind = Kind.Power;
                break;
            case '(':
                kind = Kind.LeftParen;
                break;
            case ')':
                kind = Kind.RightParen;
                break;
            case ',':
                kind = Kind.Comma;
                break;
            case '&':
                kind = Kind.Ampersand;
                break;
        }

        return new Token(kind, String.valueOf(ch), line);
    }

    //
    private Token keywordOrIdentifier()
    {
        int begin = position - 1;
        char ch = source[begin];
        while( Character.isLetterOrDigit(ch) )
            ch = source[position++];
        if( ch != '$' )
            --position;
        String vl = String.copyValueOf(source, begin, position - begin);
        Kind kd = keywords.getOrDefault(vl, Kind.Identifier);
        return new Token(kd, vl, line);
    }

    //
    private Token numericLiteral()
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
        return new Token(Kind.Number, vl, line);
    }

    private Token textLiteral()
    {
        int begin = position;
        char ch = source[begin];
        while( ch != '"' )
            ch = source[position++];
        String vl = String.copyValueOf(source, begin, position - begin - 1);
        return new Token(Kind.Text, vl, line);
    }
}
