## Ինտերպրետատորի բաղադրիչները

Բեյսիկ֊Փ լեզվի ինտերպրետատորի միջուկը բաղկացած է `Function` դասից (class), 
`Statement` ինտերֆեյսն (interface) իրականացնող հրամանների դասերից և `Expression` 
ինտերֆեյսն իրականացնող արտահայտությունների դասից։ Բոլոր այս ինտերֆեյսներն ու 
դասերը հավաքված են `engine` փաթեթում (package)։ Ըստ էության, այս դասերը 
շարահյուսական վերլուծության արդյունքում կառուցվող _աբստրակտ քերականական ծառի_
հանգույցների մոդելներն են։

`engine` փաթեթում են սահմանված նաև արտահայտությունների _հաշվարկման_ ու հրամանների 
_կատարման_ միջավայրի `Enviromnent` դասից, և կատարման ժամանակ հայտնաբերված 
սխալների `RuntimeError` դասից։


### Կատարման միջավայր

Կատարման միջավայրի նախատեսված է ինտերպրետացիայի ժամանակ փոփոխականների արժեքները 
պահելու համար։ `Environment` դասը ես իրականացրել եմ որպես `String`-`Value`
զույգերի ցուցակ։ Դրանում մեթոդներ են նախատեսված նոր փոփոխան ներմուծելու (`add`), 
գոյություն ունեցած փոփխականին նոր արժեք կապելու (`update`) և փոփոխականի արժեքը 
վերցնելու համար (`get`)։ Կատարման միջավարում անուններն ու դրանց ընթացիկ արժեքները
կազմակերպված են մեկ գծային մակարդակում, քանի որ փոփոխականի տեսանելիության տիրույթ 
(scope) է համարվում դրան առաջին անգամ արժեք վերագրելու պահից մինչև ֆունկցիայի վերջը։


### Արտահայտությունների մոդելները

Բեյսիկ-Փ լեզվի արտահայտությունները հինգ տեսակի են. _հաստատուն_ արժեքներ, 
_փոփոխականներ_, _ունար_ և _բինար_ գործողություններ, _ֆուկցիայի կանչ_։ Դրանք 
մոդելավորող բոլորն էլ դասերով էլ իրականացնում են `Expression` ինտերֆեյսը։

````
public interface Expression {
    Value evaluate( Environment env ) throws RuntimeError;
}
````

Միակ `evaluate` մեթոդն արգումենտում սպասում է կատարման միջավայրի հղում և
վերադարձնում է `Value` համապիտիանի արժեք։ Կատարման ժամանակի սխալները
հաղորդվում են `RuntimeError` դասի օգնությամբ։


#### Համապիտանի արժեք

Ինտերպրետատորի աշխատանքի ժամանակ հաշվարկված արժեքները ներկայացվում են `Value`
դասով։ Այն նախատեսված է որպես համապիտանի (ունիվերսալ) արժեքի մոդել, և թույլ է
տալիս աշխատել թվային (իրական, `double`) և տեքստային (`String`) արժեքների
հետ։

````
public class Value {
    public static final char REAL = 'R';
    public static final char TEXT = 'T';

    public char kind = REAL;

    public double real = 0.0;
    public String text = null;

    public Value( double vl )
    {
        kind = REAL;
        real = vl;
    }

    public Value( String vl )
    {
        kind = TEXT;
        text = vl;
    }
    // ...
}
````

`Value` տիպի արժեքների հետ ունար և բինաար գործողություններ կատարելու համար
 նախատեսված են երկու `calculate` մեթոդներ։ Դրանցում նախ կատարվում է արժեքների
 տիպերի դինամիկ ստուգում, ապա բուն գործողությունը։
 
````
public class Value {
     // ...
    public Value calculate( String oper ) throws RuntimeError
    {
        if( kind == TEXT )
            throw new RuntimeError("Տողի համար %s գործողություն սահմանված չէ։", oper);

        if( oper.equals("-") )
            return new Value(-real);

        if( oper.equals("NOT") )
            return new Value(real == 0 ? 1 : 0);

        return this;
    }
    // ...
}
````

Նմանատիպ մեթոդ է նախատեսված նաև բինար գործողությունների համար։

````
public class Value {
     // ...
    public Value calculate( String oper, Value vlo ) throws RuntimeError
    {
        // TODO շարունակել

        return this;
    }
    // ...
}
````


#### Թվային և տողային հաստատուններ

`Real` և `Text` դասերով մոդելավորված են ծրագրի տեքստում գրված թվային (իրական) 
ու տողային հաստատունները (լիտերալներ)։

`Real` դասը պարզապես «թաղանթ» է `double` ներդրված տիպի համար․

````
public class Real implements Expression {
    public double value = 0.0;

    public Real( double vl )
    {
        value = vl;
    }
    // ...
}
````

Իսկ `Text` դասը

````
public class Text implements Expression {
    public String value = null;

    public Text( String vl )
    {
        value = vl;
    }
    // ...
}
````



#### Փոփոխական

`Variable` դասը ծրագրի տեքստում հանդիպող անունների մոդելն է։ Այն կարող է հղվել
իրական (թվային) և տողային արժեքների վրա։ Եթե ծրագրի տեքստում փոփոխականի անունին
կցված է `$` վերջածանցը, ապա տվյալ անունը կարող է հղվել տողային արժեքների, 
հակառակ դեպքում՝ իրական թվային։

````
public class Variable implements Expression {
    public static final char REAL = 'R';
    public static final char TEXT = 'T';

    public String name = null;
    public char type = REAL;

    public Variable( String na )
    {
        type = na.endsWith("$") ? TEXT : REAL;
        name = na;
    }
    // ...
}
````


#### Ունար գործողություններ

`Unary` դասը «պատասխանատու» է իրական թվի բացասման `-` և տրամաբանական 
ժխտման `NOT` գործողությունների համար։ `operation` դաշտը գործողության
տեքստային աանվանումն է, իսկ `subexpr` դաշտը՝ այն արտահայտությունը, 
որի նկատմամբ պետք է կիրառել տրված գործողությունը։ 

````
public class Unary implements Expression {
    private String operation = null;
    private Expression subexpr = null;

    public Unary( String op, Expression se )
    {
        operation = op;
        subexpr = se;
    }

    // ...
 }
````

#### Բինար գործողություններ

````
public class Binary implements Expression {
    private String operation = null;
    private Expression subexpro = null;
    private Expression subexpri = null;

    public Binary( String op, Expression seo, Expression sei )
    {
        operation = op;
        subexpro = seo;
        subexpri = sei;
    }
    // ...
}
````


### Հրամանների մոդելները

Բեյսիկ֊Փ լեզվի հրամաններն իրականացնում են `Statement` ինտերֆեյսը։

````
public interface Statement {
    void execute( Environment env ) throws RuntimeError;
}
````

### Ֆունկցիայի մոդելը

````
public class Function {
    public String name = null;
    public List<String> parameters = null;
    public Statement body = null;

    public Function( String nm, List<String> pr, Statement bo )
    {
        name = nm;
        parameters = pr;
        body = bo;
    }
}
````






