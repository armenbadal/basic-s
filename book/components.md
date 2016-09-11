## Ինտերպրետատորի բաղադրիչները

Քերականության նկարագրությունից տեսանելի է, որ Բեյսիկ-Փ լեզվով ծրագրերը բաղկացած են 
_ֆունկցիաներից_, որոնք կազմված են _հրամաններից_ (կամ ղեկավարող կառուցվածքներից), 
վերջիններս էլ իրենց հերթին կազմված են հրամաններից ու _արտահայտություններից_։ 
Լեզվի՝ `engine` փաթեթում սահմանված միջուկը դասեր ու ինտերֆեյսներ է պարունակում
այդ երեք հիմնական բաղադրիչների համար։

Ֆունկցիայի հայտարարությունն ու սահմանումը ներկայացվում է `Function` դասով, հրամանները 
ներկայացվում էն `Statement` ինտերֆեյսն իրականացնող դասերով, իսկ արտահայտություններն 
էլ՝ `Expression` ինտերֆեյսն իրականացնող դասերով։ Ըստ էության, այս դասերը շարահյուսական 
վերլուծության արդյունքում կառուցվող _աբստրակտ քերականական ծառի_ (ԱՔԾ) հանգույցների 
մոդելներն են։

Նույն `engine` փաթեթում են սահմանված նաև արտահայտությունների _հաշվարկման_ ու 
հրամանների _կատարման_ միջավայրի `Enviromnent` դասը, ինչպես նաև կատարման 
ժամանակ հայտնաբերված սխալների ներկայացման `RuntimeError` դասը։


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

````java
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

````java
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
 
````java
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

````java
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

````java
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

````java
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

````java
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
տեքստային անվանումն է, իսկ `subexpr` դաշտը՝ այն արտահայտությունն է, որի 
նկատմամբ պետք է կիրառել տրված գործողությունը։ 

````java
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

````java
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

#### Ֆունկցիայի կիրառում

Բեյմսիկ-Փ լեզվում արգումենտների նկատմամբ հնարավոր է կիրառել կամ ներդրված 
ֆունկցիա, կամ էլ ծրագրորդի կողմից սահմանված ֆունկցիա։

Ներդրված ֆունկցիայի կիրառումը ԱՔԾ-ում ներկայացնելու համար է նախատեսված 
`BuiltIn` դասը։ Այն «գիտի» բոլոր ներդրված ֆունկցիաների անունները և «գիտի», 
թե ինչպես հաշվարկել դրանց կիրառումները։

````java
public class BuiltIn implements Expression {
    private static final String[] predefined = 
            { "SQR", "ABS", "SIN" };

    public static boolean isInternal( String nm )
    {
        for( String ir : predefined )
            if( nm.equals(ir) )
                return true;
        return false;
    }

    private String name = null;
    private List<Expression> arguments = null;

    public BuiltIn(String nm, List<Expression> ags )
    {
        name = nm;
        arguments = ags;
    }
    // ...
}
````

`predefined` ստատիկ զանգվածում թվարկված են Բեյսիկ-Փ լեզվին տվյալ պահին «ծանոթ»
ներդրված ֆունկցիաների անունները։ Օրինակ, `SQR` — թվից քառակուսի արմատ հանել, 
`ABS` — որշել թվի բացարձակ արժեքը և այլն։

Ծրագրորդի սահմանած ֆունկցիաների կիրառումները ներկայացնելու համար նախատեսված 
է `Apply` դասը։ Դրա `function` անդամը հղում է սահմանված ֆունկցիային, իսկ
`arguments` անդամը արգումենտների ցուցակն է։

````java
public class Apply implements Expression {
    public Function function = null;
    public List<Expression> arguments = null;

    public Apply( Function fu, List<Expression> ag )
    {
        function = fu;
        arguments = ag;
    }
    // ...
}
````



### Հրամանների մոդելները

Բեյսիկ-Փ լեզվի ղեկավարող կառուցվածքները (հրամանները) մոդելավորող բոլոր դասերն
իրականացնում են `Statement` ինտերֆեյսը։ Վերջինիս միակ `execute` մեթոդը
նախատեսված է հրամանների կատարման վարքը մոդելավորելու համար, և արգումենտում
սպասում է կատարման միջավայրի հղումը։

````java
public interface Statement {
    void execute( Environment env ) throws RuntimeError;
}
````

#### Վերագրում

Վերագրման հրամանի մոդելը `Let` դասն է, որի `vari` անդամը վերագրման _տեղն_ է,
իսկ `valu` անդամը՝ _վերագրվող_ արտահայտությունը։

````java
public class Let implements Statement {
    private Variable vari = null;
    private Expression valu = null;

    public Let(Variable vn, Expression vl )
    {
        vari = vn;
        valu = vl;
    }
    // ...
}
````

#### Տվյալների ներմուծում

Ներմուծման ստանդարտ հոսքից տվյալները փոփոխականի մեջ կարդալու `INPUT` հրամանը
մոլելավորված է `Input` դասով։ Դրա `varname` անդամն այն փոփոխականն է, որին 
կատարման միջավայրում պետք է կապվի ներմուծման հոսքից կարդացած արժեքը։ 

````java
public class Input implements Statement {
    private Variable varname = null;

    public Input( Variable vn )
    {
        varname = vn;
    }
    // ...
}
````


#### Տվյալների արտածում

Արտածման `PRINT` հրամանի մոդելը `Print` դասն է, որի `priex` անդամը արտածվելից
արտահայտության հղումն է։

````java
public class Print implements Statement {
    private Expression priex = null;

    public Print( Expression exp )
    {
        priex = exp;
    }
    // ...
}
````


#### Ճյուղավորում

Բեյսիկ-Փ լեզվում ծրագրի կատարման ընթացքում ճյուղավորումներ են մտցվում `IF` ղեկավարող 
կառուցվածքով։ Ճյուղավորումը մոդելավորող `If` դասում երեք անդամներ են. `condition` - 
ճյուղավորման պայմանը, `decision` - պայմանի ճշմարիտ լինելու դեպքում կատարվող բլոկը,
`alternative` - պայմանի կեղծ լինելու դեպքում կատարվող բլոկը։ Քանի որ `IF` կառուցվածքը
ծրագրերում հանդիպում է իր «կարճ» ու «երկար» տարբերակներով, `If` դասի կոնստրուկտորը
գրել եմ միայն կարճ տարբերակի համար, իսկ `ELSE` (`ELSEIF`) ճյուղի `alternative`
հղումն արժեքավորելու համար նախատեսել եմ `setElse()` մեթոդը։

````java
public class If implements Statement {
    private Expression condition = null;
    private Statement decision = null;
    private Statement alternative = null;

    public If( Expression co, Statement de )
    {
        condition = co;
        decision = de;
    }

    public void setElse( Statement al )
    {
        alternative = al;
    }
    // ...
}
````

#### Պարամետրով ցիկլ

Պարամետրով ցիկլի `FOR` հրամանն ունի հինգ բաղադրիչ. ցիկլի պարամետրը, պարամետրի 
սկզբնական արժեքը, պարամետրի վերջնական արժեքը, պարամետրի փոփոխման քայլը և ցիկլի
մարմինը։ Դրանք համապատասխանաբար `For` դասի `param`, `initial`, `limit`, `step`
և `body` անդամներն են։

````java
public class For implements Statement {
    private Variable param = null;
    private Expression initial = null;
    private Expression limit = null;
    private Expression step = null;
    private Statement body = null;

    public For( Variable pr, Expression in, Expression li, Expression sp, Statement be )
    {
        param = pr;
        initial = in;
        limit = li;
        step = sp;
        body = be;
    }
    // ...
}
````


#### Նախապայմանով ցիկլ

````java
public class While implements Statement {
    private Expression condition = null;
    private Statement body = null;

    public While(Expression cond, Statement bdy )
    {
        condition = cond;
        body = bdy;
    }
    // ...
}
````

#### Ենթածրագրի կանչ

#### Հրամանների հաջորդում



### Ֆունկցիայի մոդելը

Բեյսիկ-Փ լեզվով գրված ֆունկցիան որոշվում է անունով, պարամետրերի ցուցակով և մարմինը 
կազմող հրամաններով։ `Function` դասը ֆունկցիայի մոդելն է. դրա `name` դաշտը անունն 
է, `parameters` դաշտը պարամետրերի ցուցակը և `body` դաշտն էլ՝ մարմինը։ 
Կոնստրուկտորը սպասում է միայն ֆունցկայի անունն ու պարամետրերի ցուցակը, քանի որ
`Function` օբյեկտ է կառուցվում և՛ ֆունկցիան հայտարարելիս (`DECLARE`), և՛ սահմանելիս։

````
public class Function {
    public String name = null;
    public List<Variable> parameters = null;
    public Statement body = null;

    public Function( String nm, List<String> pr )
    {
        name = nm;
        parameters = pr;
    }
    // ...
}
````
