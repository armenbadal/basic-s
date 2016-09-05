## Շարահյուսական վերլուծություն

Բեյսիկ-Փ լեզվի շարահյուսական վերլուծիչն (syntax analyzer) իրականացված է `parser` 
փաթեթի `Parser` դասով։ Նույն `parser` փաթեթում են ներառված նաև բառային վերլուծիչի 
(lexical analyzer) `Scanner` դասը, տերմինալային սիմվոլների `Token` թվարկումը, 
ծրագրի տեքստի լեքսեմի `Lexeme` դասը, ինչպես նաև շարահյուսական սխալների ազդարարման
`ParseError` դասը։

Շարահյուսական վերլուծության արդյունքում կառուցվում է _աբստրակտ քերականական ծառ_, 
որի հանգույցները `engine` փաթեթի դասերի նմուշներն (instance) են (տես 
[Ինտերպրետատորի բաղադրիչները](components.md) բաժինը)։ Եթե վերլուծության ժամանակ 
հայտնաբերվում է որևէ սխալ, ապա այդ մասին ազդարարվում է `ParseError` դասի նմուշի 
միջոցով և վերլուծությունը դադարեցվում է։ (Վերլուծության սխալների մշակման մի ստրատեգիայի 
մասին նկարագրված է Նիկլաուս Վիրտի «Կիմպիլյատորի կառուցումը» գրքում։ Ընթերցողին 
առաջարկում եմ Բեյսիկ-Փ լեզվի շարահյուսական վերլուծիչում իրականացնել այդ մոտեցումը։)


### Բառային վերլուծություն

Շարահյուսական վերլուծիչն իր աշխատանքում օգտագործում է _բառային վերլուծիչը_ (լեքսիկական
անալիզատոր), որպեսզի ծրագրի տեքստից կարդա հերթական _թոքեն-լեքսեմ_ զույգը։ Բառային 
վերլուծիչն իրականացրել եմ `Scanner` դասում։ Դրա կոնստրուկտորը ստանում է վերլուծության 
ենթական տեքստը, իսկ `next()` մեթոդը, ամեն մի կանչի արդյունքում, վերադարձնում է տեքստից 
կարդացած հերթական լեքսեմը՝ `Lexeme` նասի նմուշ։

`Lexeme` դասը իրար է կապում `Token` թվարկումով (enumeration) սահմանված _թոքենն_ 
(`kind` անդամը) ու ծրագրի տեքստից կարդացած հատվածը՝ _լեքսեմը_ (`value` անդամը)։ 
Իսկ `line` անդամը լեքսեմի տողի համարն է ծրագիր տեքստում։

````
public class Lexeme {
    public Token kind = Token.Unknown;
    public String value = null;
    public int line = 0;

    public Lexeme( Token kn, int ps )
    {
        this(kn, null, ps);
    }

    public Lexeme( Token kn, String vl, int ps )
    {
        kind = kn;
        value = vl;
        line = ps;
    }
    // ...
}
````

Շարահյուսական վերլուծության ժամանակ հաճախ պետք է լինում ստուգել, թե արդյոք հերթական
լեքսեմի տիպը (`kind`) տրված թոքեններից որևէ մեկնէ։ Դրա համար նախատեսված է `is()`
վարիադիկ մեթոդը։

````
public class Lexeme {
    // ...
    public boolean is( Token... exps )
    {
        for( Token ex : exps )
            if( kind == ex )
                return true;
        return false;
    }
    // ...
}
````

`Token` թվարկման մեջ հավաքված են Բեյսիկ-Փ լեզվի քերականության տերմինալային սիմվոլների 
անունները։ Դրանք ես խմբավորել եմ ըստ նշանակության (ծառայողական բառեր, գործողություններ 
և այլն)։

````
public enum Token {
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

Վերադառնամ `Scanner` դասին։ Արդեն մի քանի անգամ նշեցի, որ այն պետք է կարողանա
_ճանաչել_ Բեյսիկ-Փ լեզվի քերականության տերմինալային սիմվոլների բազմությունը, որի 
ենթաբազմություն է ծառայողական բառերի բազմությունը։ `keywords` ստատիկ անդամը,
որը `String`→`Token` արտապատկերում է, `Scanner` դասի ստատիկ արժեքավորման 
բլոկում լրացվում է ծառայողական բառերի ու դրանց համապատասխան թոքենների զույգերով։

````
public class Scanner {
    // ...
    private static Map<String,Token> keywords = null;
    static {
        keywords = new HashMap<>();
        keywords.put("DECLARE", Token.Declare);
        keywords.put("FUNCTION", Token.Function);
        keywords.put("END", Token.End);
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
    // ...
}
````

`Scanner` դասի կոնստրուկտորը վերցնում է վերլուծվող տեքստը, դրանից ստանում է նիշերի (`char`)
զանգված և վերագրում է դասի `source` անդամին։ `position` անդամը հերթական դիտարկվող նիշն է,
իսկ `line` անդամն էլ՝ հերթական տողը։

````
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

Հիմա ամենագլխավորի՝ `next()` մեթոդի մասին։ Արդեն նշեցի, որ այն տեքստից (ավելի ճիշտ՝ `source`
զանգվածից) կարդում և վերադարձնում է հերթական լեքսեմ-թոքեն զույգը։ `next()` մեթոդը, ինչպես 
ընդունված է բառային վերլուծիչներում, աշխատում է  _վերջավոր ավտոմատի_ մոդելավորման սկզբունքով.
կարդում է հերթական սիմվոլը, դրանով որոշում է, թե ինչ հաջորդականություն պետք է կարդա, կարդում
է այդ հաջորդականությունը և վերադարձնում է համապատասխան `Lexeme` օբյեկտը։ Բացի այդ, `next()`
մեթոդի պարտականությունն է նաև ակրդալ ու անտեսել տեքստում հանդիպող բացատանիշերն ու Բեյսիկ-Փ
լեզվի մեկնաբանությունները։ Հետևյալ հատվածում ցուցադրված են այդ առաջին քայլերը.

````
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
    // ...
}      
````

Հաջորդ հատվածում ծառայողական բառերի ու իդենտիֆիկատորների, ինչպես նաև թվային
ու տեքստային լիտերալների վերլուծությունն է։ Դրանցից յուրաքանչյուրի համար ես 
առանձին մեթոդ եմ գրել։

````
public Lexeme next()
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



````
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
````

### Վերլուծության ռեկուրսիվ վայրէջքի եղանակ


Շարահյուսական վերլուծությունն իրականացրել եմ _ռեկուրսիվ վայրէջքի_ (recursive descent)
եղանակով։ Որպեսզի պարզ լինի, թե ինչպես է կառուցվել ամբողջ Բեյսիկ-Փ լեզվի վերլուծիչը,


Դիտարկենք, օրինակ, թվաբանական արտահայտությունների լեզվի քերականությունը, որտեղ 
տերմինալային սիմվոլներն են `+`, `-`, `*`, `/`, `(`, `)` և `NUMBER`, իսկ ոչ 
տերմինալայինները՝ `Expr`, `Term` և `Factor`։

````
Expr   = Term { ('+'|'-') Term }.
Term   = Factor { ('*'|'/') Factor }.
Factor = '(' Expr ')'
       | NNUMBER.
````

Քերականության ամեն մի ոչ տերմինալային սիմվոլի համար վերլուծիչի ծրագրում նախատեսվում 
է մի պրոցեդուրա, որի մարմինը ձևավորվում է քերականական կանոնի ձախ կողմի անդամների 
շղթայից։ Եթե հերթական սիմվոլը ոչ տերմինալ է, ապա կանչվում է դրան համապատասխան 
պրոցեդուրան։ Եթե տերմինալային սիմվոլ է, ապա բառային անալիզատորից կարդացվում է 
հերթական թոքենը։ Օրինակ, `Expr` ոչ տերմինալը սահմանող կանոնի համար պետք է գրել
հետևյալը.

````
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

````
private void parseTerm()
{
    parseFactor();
    while( look == '*' || look == '*' ) {
        look = nextToken();
        parseFactor();
    }
}
````

````
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

`look` սիմվոլը, որը բերված պրոցեդուրաներում օգտագործված է վերլուծության ընթացքը,
ուղղությունը ճշտելու համար, կոչվում է _look a head_, այսինքն սիմվոլ, որով կարելի 
է _առաջ նայել_ և որոշում կայացնել։ 


