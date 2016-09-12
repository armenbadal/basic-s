## Շարահյուսական վերլուծություն

Բեյսիկ-Փ լեզվի շարահյուսական վերլուծիչն (syntax analyzer) իրականացված է
`parser` փաթեթի `Parser` դասով։ Նույն `parser` փաթեթում են ներառված
նաև բառային վերլուծիչի (lexical analyzer) `Scanner` դասը, տերմինալային
սիմվոլների `Kind` թվարկումը, ծրագրի տեքստի թոքեն-լեքսեմ զուգի `Token`
դասը, ինչպես նաև շարահյուսական սխալների ազդարարման `ParseError` դասը։

Շարահյուսական վերլուծության արդյունքում կառուցվում է _աբստրակտ քերականական
ծառ_, որի հանգույցները `engine` փաթեթի դասերի նմուշներն (instance) են (տես 
[Ինտերպրետատորի բաղադրիչները](components.md) բաժինը)։ Եթե վերլուծության 
ժամանակ հայտնաբերվում է որևէ սխալ, ապա այդ մասին ազդարարվում է `
ParseError` դասի նմուշի միջոցով և վերլուծությունը դադարեցվում է։ (Վերլուծության 
սխալների մշակման մի ստրատեգիայի մասին նկարագրված է Նիկլաուս Վիրտի 
«Կիմպիլյատորի կառուցում» գրքում։ Ընթերցողին առաջարկում եմ Բեյսիկ-Փ լեզվի 
շարահյուսական վերլուծիչում իրականացնել այդ մոտեցումը։)


### Բառային վերլուծություն

Շարահյուսական վերլուծիչն իր աշխատանքում օգտագործում է _բառային վերլուծիչը_
(լեքսիկական անալիզատոր), որպեսզի ծրագրի տեքստից կարդա հերթական
_թոքեն-լեքսեմ_ զույգը։ Բառային վերլուծիչն իրականացրել եմ `Scanner` դասում։
Դրա կոնստրուկտորը ստանում է վերլուծության ենթական տեքստը, իսկ `next()`
մեթոդը, ամեն մի կանչի արդյունքում, վերադարձնում է տեքստից կարդացած
հերթական թոքենը՝ `Token` նասի նմուշ։

`Token` դասը իրար է կապում `Kind` թվարկումով (enumeration) սահմանված
_թոքենն_ (`kind` անդամը) ու ծրագրի տեքստից կարդացած հատվածը՝ _լեքսեմը_
(`lexeme` անդամը)։ Իսկ `line` անդամը լեքսեմի տողի համարն է ծրագիր 
տեքստում։

````java
public class Token {
    public Token kind = Token.Unknown;
    public String lexeme = null;
    public int line = 0;

    public Token( Token kn, int ps )
    {
        this(kn, null, ps);
    }

    public Token( Token kn, String vl, int ps )
    {
        kind = kn;
        lexeme = vl;
        line = ps;
    }
    // ...
}
````

Շարահյուսական վերլուծության ժամանակ հաճախ պետք է լինում ստուգել, թե արդյոք
հերթական լեքսեմի տիպը (`kind`) տրված թոքեններից որևէ մեկնէ։ Դրա համար
նախատեսված է `is()` վարիադիկ մեթոդը։

````java
public class Token {
    // ...
    public boolean is( Kind... exps )
    {
        for( Kind ex : exps )
            if( kind == ex )
                return true;
        return false;
    }
    // ...
}
````

`Kind` թվարկման մեջ հավաքված են Բեյսիկ-Փ լեզվի քերականության տերմինալային
սիմվոլների անունները։ Դրանք ես խմբավորել եմ ըստ նշանակության (ծառայողական
բառեր, գործողություններ և այլն)։

````java
public enum Kind {
    // անծանոթ է 
    Unknown,
     
    // թիվ, տեքստ, իդենտիֆիկատոր
    Number, Text, Identifier,
    
    // ծառայողական բառեր 
    Declare, Function, End, Let, Input, Print, If,
    Then, ElseIf, Else, For, To, Step, While, Call,

    // կետադրական նշաններ
    LeftParen, RightParen, Comma,

    // տրամաբանական գործողությունների անուններ
    Or, And, Not,

    // համեմատման գործողություններ
    Eq, Ne, Gt, Ge, Lt, Le,
    
    // թվաբանական գործողություններ
    Add, Sub, Mul, Div, Power,

    // նոր տողի նիշ, տեքստի ավարտ 
    NewLine, Eos
}

````

Վերադառնամ `Scanner` դասին։ Արդեն մի քանի անգամ նշեցի, որ այն պետք է
կարողանա _ճանաչել_ Բեյսիկ-Փ լեզվի քերականության տերմինալային սիմվոլների
բազմությունը, որի ենթաբազմություն է ծառայողական բառերի բազմությունը։
`keywords` ստատիկ անդամը, որը `String`→`Kind` արտապատկերում է,
`Scanner` դասի ստատիկ արժեքավորման բլոկում լրացվում է ծառայողական 
բառերի ու դրանց համապատասխան թոքենների զույգերով։

````java
public class Scanner {
    // ...
    private static Map<String,Kind> keywords = null;
    static {
        keywords = new HashMap<>();
        keywords.put("DECLARE", Kind.Declare);
        keywords.put("FUNCTION", Kind.Function);
        keywords.put("END", Kind.End);
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
    // ...
}
````

`Scanner` դասի կոնստրուկտորը վերցնում է վերլուծվող տեքստը, դրանից ստանում
է նիշերի (`char`) զանգված և վերագրում է դասի `source` անդամին։ `position`
անդամը հերթական դիտարկվող նիշն է, իսկ `line` անդամն էլ՝ հերթական տողը։

````java
public class Scanner {
    // ...
    private char[] source = null;
    private int position = 0;

    public int line = 1;

    public Scanner( String text )
    {
        source = text.toCharArray();
    }
    // ...
}
````

Հիմա ամենագլխավորի՝ `next()` մեթոդի մասին։ Արդեն նշեցի, որ այն տեքստից
(ավելի ճիշտ՝ `source` զանգվածից) կարդում և վերադարձնում է հերթական
թոքեն-լեքսեմ զույգը։ `next()` մեթոդը, ինչպես ընդունված է բառային
վերլուծիչներում, աշխատում է  _վերջավոր ավտոմատի_ մոդելավորման սկզբունքով.
կարդում է հերթական սիմվոլը, դրանով որոշում է, թե ինչ հաջորդականություն պետք
է կարդա, կարդում է այդ հաջորդականությունը և վերադարձնում է համապատասխան
`Token` օբյեկտը։ Բացի այդ, `next()` մեթոդի պարտականությունն է նաև կարդալ 
ու անտեսել տեքստում հանդիպող բացատանիշերն ու Բեյսիկ-Փ լեզվի
մեկնաբանությունները։ Հետևյալ հատվածում ցուցադրված են այդ առաջին քայլերը.

````java
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
    // ...
}      
````

Հաջորդ հատվածում ծառայողական բառերի ու իդենտիֆիկատորների, ինչպես նաև
թվային ու տեքստային լիտերալների վերլուծությունն է։ Դրանցից յուրաքանչյուրի
համար ես առանձին մեթոդ եմ գրել։

````java
public Token next()
{
    // ...
    // ծառայողական բառեր և իդենտիֆիկատոր
    if( Character.isLetter(ch) )
        return keywordOrIdentifier();

    // թվային լիտերալ
    if( Character.isDigit(ch) )
        return numericLiteral();

    // տողային լիտերալ
    if( ch == '"' )
        return textLiteral();
    // ...
}      
````

Ծառայողական բառերն ու իդենտիֆիկատորները կարդում է `keywordOrIdentifier()`
մեթոդը։ Հերթական իդենտիֆիկատորը կարդալուց հետո այն որոնվում է `keywords`
ցուցակում։ Եթե առկա է, ապա վերադարձվում է ծառայողական բառին
համապատասխան լեքսեմը, հակառակ դեպքում՝ իդենտիֆիկատորին համապատասխան
լեքսեմը։


````java
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
````

Թվային լիտերալն այստեղ իրականացրել եմ իր ամենապարզ տեսքով.
`[0-9]+(.[0-9]+)?`։ `numericLiteral()` մեթոդը կարդում է այդ տեսքի թվային
(իրական) լիտերալները։

````java
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
````

Տեքստային լիտերալները սկսվում և ավարտվում են զույգ չակերտով (`"`) և կարող
են պարունակել կամայական նիշեր՝ բացի չակերտից։ `textLiteral()` մեթոդը
կարդում է տեքստային մեկ տեքստային լիտերալ և վերադարձնում է համապատասխան
`Token` օբյեկտ։ (Ընթերցողին ուզում եմ հուշել, որ այս մեթոդում թերություն (bug)
կա․ թույլատրվում է տեքստային լիտերալում կարդալ նոր տողի անցման նիշը։ Նախ՝
դա չի համապատասխանում BASIC֊ի կանոններն, ապա՝ խառնում է տողերի հաշիվը։)

````java
private Token textLiteral()
{
    int begin = position;
    char ch = source[begin];
    while( ch != '"' )
        ch = source[position++];
    String vl = String.copyValueOf(source, begin, position - begin);
    return new Token(Kind.Text, vl, line);
}
````

`Scanner` դասի `next()` մեթոդի հաջորդ բլոկում մշակվում են համեմատման 
 գործողությունների նիշերը։ Այստեղ բացատրելու բան էլ չկա։
 
````java
public Token next()
{
    // ...
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
    // ...
 }
    
````

Վերջապես, 

````java
public Token next()
{
    // ...
    Kind kind = Kind.Unknown;
    switch( ch ) {
        case '=': kind = Kind.Eq;         break;
        case '+': kind = Kind.Add;        break;
        case '-': kind = Kind.Sub;        break;
        case '*': kind = Kind.Mul;        break;
        case '/': kind = Kind.Div;        break;
        case '^': kind = Kind.Power;      break;
        case '(': kind = Kind.LeftParen;  break;
        case ')': kind = Kind.RightParen; break;
        case ',': kind = Kind.Comma;      break;
    }

    return new Token(kind, String.valueOf(ch), line);
}
````
 

### Վերլուծության ռեկուրսիվ վայրէջքի եղանակ


Շարահյուսական վերլուծությունն իրականացրել եմ _ռեկուրսիվ վայրէջքի_ (recursive
descent) եղանակով։ Որպեսզի պարզ լինի, թե ինչպես է կառուցվել ամբողջ Բեյսիկ-Փ
լեզվի վերլուծիչը,


Դիտարկենք, օրինակ, թվաբանական արտահայտությունների լեզվի քերականությունը,
որտեղ տերմինալային սիմվոլներն են `+`, `-`, `*`, `/`, `(`, `)` և `NUMBER`,
իսկ ոչ տերմինալայինները՝ `Expr`, `Term` և `Factor`։

````
Expr   = Term { ('+'|'-') Term }.
Term   = Factor { ('*'|'/') Factor }.
Factor = '(' Expr ')'
       | NNUMBER.
````

Քերականության ամեն մի ոչ տերմինալային սիմվոլի համար վերլուծիչի ծրագրում
նախատեսվում է մի պրոցեդուրա, որի մարմինը ձևավորվում է քերականական
կանոնի ձախ կողմի անդամների շղթայից։ Եթե հերթական սիմվոլը ոչ տերմինալ
է, ապա կանչվում է դրան համապատասխան պրոցեդուրան։ Եթե տերմինալային
սիմվոլ է, ապա բառային անալիզատորից կարդացվում է հերթական թոքենը։ 
Օրինակ, `Expr` ոչ տերմինալը սահմանող կանոնի համար պետք է գրել հետևյալը.

````java
private void parseExpr()
{
    parseTerm();
    while( look == '+' || look == '-' ) {
        look = nextToken();
        parseTerm();
    }
}
````

Իսկ `Term` և `Factor` կանոնների համար պետք է գրել հետևյալ պրոցեդուրաները.

````java
private void parseTerm()
{
    parseFactor();
    while( look == '*' || look == '*' ) {
        look = nextToken();
        parseFactor();
    }
}
````

````java
private void parseFactor()
{
    if( look == '(' ) {
        look = nextToken();
        parseExpr();
        look = nexToken();
    }
    else if( look == NUMBER )
        look = nextToken();
}
````

`look` սիմվոլը, որը բերված պրոցեդուրաներում օգտագործված է վերլուծության
ընթացքը, ուղղությունը ճշտելու համար, կոչվում է _look a head_, այսինքն
սիմվոլ, որով կարելի է _առաջ նայել_ և որոշում կայացնել։ 


