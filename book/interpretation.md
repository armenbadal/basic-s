## Ինտերպրետացիա

[Ինտերպրետատորի բաղադրիչները](components.md) բաժնում ես ներկայացրեցի 
Բեյսիկ-Փ լեզվի միջուկը պարունակող `engine` փաթեթի դասերն ու ինտերֆեյսները։ Այս 
բաժնում արդեն ես մեկ առ մեկ ներկայացնում եմ բոլոր այդ դասերի ինտերպրետացիան։

Հիշենք, որ շարահյուսական վերլուծության արդյունքում կառուցվում է աբստրակտ 
քերականական ծառ, որի հանգույցները `Expression` և `Statement` ինտերֆեյսներն
իրականացնող դասերի, ինչպես նաև `Function` դասի, նմուշներ են։ Պետք է անցնել 
այդ ծառի հանգույցներով և _ինտերպրետացնել_ (արտահայտությունները՝ հաշվարկել — 
evaluate, իսկ հրամանները՝ կատարել — execute) դրանք։


### Արտահայտությունների հաշվարկը

`Expression` ինտերֆեյսը պարունակում է միակ `evaluate()` մեթոդը, որն արգումենտում 
սպասում է կատարման միջավայրը (environment) և վերադարձնում է համապիտանի արժեք՝ 
`Value`։ Հենց այս մեթոդի իրականացումով է ապահովված բոլոր արտահայտությունների
ինտերպրետացաիան։


#### Իրական թվեր և տեքստային հաստատուններ

Իրական թվերը մոդելավորող `Real` դասի և տեքստային հաստատունները մոդելավորող `Text`
դասի նմուշների ինտերպրետացիան հասարակ է. պարզապես ստեղծվում է դրանց `value` 
դաշտերի արժեքը պարունակող `Value` օբյեկտ։ (`Value` դասն ունի համապատսխան
կոնստրուկտորները։)


#### Փոփոխականներ

Փոփոխականի հաշվարկը պետք է վերադարձնի դրա ընթացիկ արժեքը, որը պահվում է կատարման
միջավայրում։ Եթե կատարման միջավայրում տվյալ փոփոխականին արժեք համապատասխանեցված
չէ, ապա ազդարարվում է սխալի մասին։

````java
public class Variable implements Expression {
    // ...
    @Override
    public Value evaluate( Environment env ) throws RuntimeError
    {
        Value val = env.get(this);
        if( val == null )
            throw new RuntimeError(String.format("Չսահմանված փոփոխական %s", name));
        return val;
    }
    // ...
}
````


#### Ունար և բինար գործողություններ

Բեյսիկ-Փ լեզվում նախատեսված են երկու ունար գործողություններ՝ `-` և `NOT`, երկուսն էլ որոշված 
միայն թվերի համար։ Դրանք հաշվարկելու համար պետք է նախ հաշվարկել գործողության 
ենթաարտահայտությունը, ապա գործողությունը կիրառել ստացված արժեքի նկատմամբ։

````java
public class Unary implements Expression {
    // ...
    @Override
    public Value evaluate( Environment env ) throws RuntimeError
    {
        Value res = subexpr.evaluate(env);
        if( operation.equals("-") )
            res = new Value(-res.real);
        else if( operation.equals("NOT") )
            res = new Value(res.real != 0 ? 0 : 1);
        return res;
    }
    // ...
}
````

Բինար գործողությունների դեպքում էլ պետք է նախ հաշվարկել երկու ենթաարտահայտությունները, 
ապա բինար գործողությունը կիրառել դրանց արժեքների նկատմամբ, ու վերադարձնել արդյունքը։
Բինար գործողություններից միայն տեքստերի կցման `&` գործողությունն է որոշված տեքստային
արժեքների համար. դա պետք է հաշվի առնել և, եթե տողերի նկատմամբ կիրառված է որևէ այլ
գործողություն, ազդարարել սխալի մասին։

````java
public class Binary implements Expression {
    // ...
    @Override
    public Value evaluate( Environment env ) throws RuntimeError
    {
        Value res0 = subexpro.evaluate(env);
        Value res1 = subexpri.evaluate(env);

        // տեքստային գործողություն
        if( res0.kind == Value.TEXT && res1.kind == Value.TEXT ) {
            if( operation.equals("&") )
                return new Value(res0.text + res1.text);
            else
                throw new RuntimeError("Տեքստերի համար %s գործողությունը որոշված չէ։",
                        operation);
        }
        // ...
````

Թվային արժեքների հետ կատարվում են թվաբանական, համեմատման ու տրամաբանական 
գործողություններ։ Քանի որ Բեյսիկ-Փ լեզվում բուլյան (տրամաբանական) տիպ նախատեսված
չէ, բուլյան _ճշմարիտ_ և _կեղծ_ արժեքները մոդելավորված են համապատսխանաբար `1` և
`0` իրական (թվային) արժեքներով։

````java
        // ...
        // թվային գործողություններ
        double resval = 0.0;
        switch( operation ) {
            case "+":
                resval = res0.real + res1.real;
                break;
            case "-":
                resval = res0.real - res1.real;
                break;
            case "*":
                resval = res0.real * res1.real;
                break;
            case "/":
                if( res1.real == 0.0 )
                    throw new RuntimeError("Բաժանում զրոյի վրա։");
                resval = res0.real / res1.real;
                break;
            case "^":
                resval = Math.pow(res0.real, res1.real);
                break;
            case "=":
                resval = res0.real == res1.real ? 1.0 : 0.0;
                break;
            case "<>":
                resval = res0.real != res1.real ? 1.0 : 0.0;
                break;
            case ">":
                resval = res0.real > res1.real ? 1.0 : 0.0;
                break;
            case ">=":
                resval = res0.real >= res1.real ? 1.0 : 0.0;
                break;
            case "<":
                resval = res0.real < res1.real ? 1.0 : 0.0;
                break;
            case "<=":
                resval = res0.real <= res1.real ? 1.0 : 0.0;
                break;
            case "AND":
                resval = (res0.real != 0) && (res1.real != 0) ? 1.0 : 0.0;
                break;
            case "OR":
                resval = (res0.real != 0) || (res1.real != 0) ? 1.0 : 0.0;
                break;
        }

        return new Value(resval);
    }
    // ...
}
````


#### Ֆունկցիայի կանչ

Բեյսիկ֊Փ լեզվում արգումենտների նկատմամբ հնարավոր է կիրառել ներդրված ֆունկցիաները
և ծրագրավորողի սահմանած ֆունկցիաները։ Բնականաբար այդ երկու տիպի ֆունկցիաների 
կիրառումները շարահյուսորեն նույնական են․ ֆունկցիայի անունը, որին հետևում է փակագծերի
մեջ առնված արգումենտների ցուցակը։ Իրականացումները, սակայն, տարբեր են․ ներդրված
ֆունկցիաների կանչի համար ստեղծվում է `Builtin` օբյեկտ, իսկ ծրագրավորողի սահմանած
ֆունկցիաների համար՝ `Apply` օբյեկտ։ 

`BuiltIn` դասի `evaluate()` մեթոդում ընտրություն է կատարվում ֆունկցիայի անունով,
հաշվարկվում է արգումենտը, ապա հաշվարկվում է ներդրված ֆունկցիային համապատասխան
գործշողությունը։ Օրինակ, `SQR` (իրական արժեքից քառակուսի արմատ) ֆունկցիայի համար
օրգտագործվում է Ջավա լեզվի `Math.sqrt()` մեդոդը։

````java
public class BuiltIn implements Expression {
    // ...
    @Override
    public Value evaluate( Environment env ) throws RuntimeError
    {
        if( name.equals("SQR") ) {
            Value a0 = arguments.get(0).evaluate(env);
            return new Value(Math.sqrt(a0.real));
        }

        // ...
        
        return null;
    }
    // ...
}
````

Սկզբունքորեն այլ է `Apply` դասի նմուշի հաշվարկումը։ Այստեղ կիրառվող ֆունկցիան
ծրագրավորողի կողմից սահմանված ֆունկցիայի մոդել է՝ `Function` դասի նմուշ։ Նախ՝
ստեղծվում է նոր լոկալ կատարման միջավայր և դրա մեջ ֆունկցիայի ամեն մի պարամետրին 
համապատասխանեցվում է փոխանցված արգումենտի հաշվարկված արժեքը։ Արգումենտները
հաշվարկվում են `evaluate()` մեթոդին փոխանցված կատարման միջավայրում։ Հետո
ֆունկցիայի մարմինը _կատարվում_ է՝ օգտագործելով լոկալ կատարման միջավայրը։ Եվ 
վերջում, որպես ֆունկցիայի արժեք, վերադարձվում է ֆունկցիայի անունով փոփոխականի
արժեքը։

````java
public class Apply implements Expression {
    // ...
    @Override
    public Value evaluate( Environment env ) throws RuntimeError
    {
        // լոկալ կատարման միջավայր
        Environment envloc = new Environment();

        // լոկալ միջավայրում պարամետրերին համապատասխանեցվում են
        // փոխանցված արգումենտների հաշվարկված արժեքները
        int i = 0;
        for( Variable pri : function.parameters ) {
            Value avo = arguments.get(i++).evaluate(env);
            envloc.add(pri, avo);
        }

        // ֆունկցիայի մարմինը կատարվում է լոկալ կատարման միջավայրում
        function.body.execute(envloc);
        // որպես արժեք վերադարձվում է լոկալ միջավայրում ֆունկցիայի անունով
        // փոփոխականի արժեքը
        return envloc.get(new Variable(function.name));
    }
    // ...
}
````


### Ղեկավարող կառուցվածքների կատարումը

Ղեկավարող կառուցվածքների կատարումը ծրագրավորելու համար`Statement` ինտերֆեյսում
նախատեսել եմ `execute()` մեթոդը, որը, ինչպես `Expression` ինտերֆեյսի `evaluate()`
մեթոդը, արգումենտում ստանում է կատարման միջավայրի հղումը։ `execute()` մեթոդների
կատարման ժամանակ փոփոխականների ընթացիկ արժեքները հարցվում են կատարման միջավայրից, 
և այդտեղ են գրանցվում փոփխականների թարմ արժեքները։

#### Վերագրման հրաման

`Let` դասով մոդելավորված վերագրման հրամանի միջոցով կատարման միջավայրում ստեղծվում 
են նոր փոփոխական-արժեք կապեր, կամ փոխվում են արդեն գոյություն ունեցող կապերի 
արժեքները։ `execute()` մեթոդում նախ հաշվարկվում է վերագրվող արտահայտության արժեքը,
ապա կատարման միջավայրի `update()` մեթոդով վերագրվող փոփոխականին է կապվում այդ
արժեքը։

````java
public class Let implements Statement {
    // ...
    @Override
    public void execute( Environment env ) throws RuntimeError
    {
        Value val = valu.evaluate(env);
        env.update(vari, val);
    }
    // ...
}
````


#### Ներմուծման և արտածման հրամաններ

`Input` դասով մոդելավորված ներմուծման հրամանը ներմուծման ստանդարտ հոսքից կարդում 
է իրական թիվ կամ տեքստի տող, և այդ կարդացած արժեքը կատարման միջավայրում կապում 
է նշված փոփոխականի հետ։

````java
public class Input implements Statement {
    // ...
    @Override
    public void execute( Environment env )
    {
        System.out.print("? ");
        java.util.Scanner scan = new java.util.Scanner(System.in);
        if( vari.isText() ) {
            String value = scan.nextLine();
            env.update(vari, new Value(value));
        }
        else {
            double value = scan.nextDouble();
            env.update(vari, new Value(value));
        }
    }
    // ...
}
````

Տվյալների արտածման հրամանը, որի մոդելը `Print` դասն է, արտածման ստանդարտ հոսքին 
է դուրս բերում իր արգումենտում տրված արտահայտության արժեքը։

````java
public class Print implements Statement {
    // ...
    @Override
    public void execute( Environment env ) throws RuntimeError
    {
        Value resv = priex.evaluate(env);
        System.out.println(resv);
    }
    // ...
}
````

#### Ճյուղավորման հրաման

`IF` կառուցվածքի ինտերպրետացիան սկսվում է ճյուղավորման պայմանի հաշվարկմամբ։ Այնուհետև,
եթե պայմանի արժեքը տարբեր է զրոյից, կատարվում է `decision` անդամին կապված ճյուղը, 
հակառակ դեպքում՝ `alternative` անդամին կապվածը։

````java
public class If implements Statement {
    // ...
    @Override
    public void execute( Environment env ) throws RuntimeError
    {
        Value conval = condition.evaluate(env);
        if( conval.real != 0.0 )
            decision.execute(env);
        else {
            if( alternative != null )
                alternative.execute(env);
        }
    }
    // ...
}
````

#### Կրկնման հրամաններ

Կրկնման հրամանները երկուսն են՝ `FOR` և `WHILE`։ Առաջինը պարամետրով ցիկլն է,
երկրորդը՝ նախապայմանով։ `FOR` կառուցվածքի մոդել `For` դասի `execute()` մեթոդում
նախ հաշվարկվում են պարամետրի սկզբնական արժեքը, սահմանային արժեքը և քայլի արժեքը
Հետո, քանի դեռ պարամետրի արժեքը փոքր է կամ հավասար սահմանային արժեքից, 
կատարվում է ցիկլի մարմինը, և պարամետրի ընթացիկ արժեքին գումարվում է պարամետրի 
քայլը։

````java
public class For implements Statement {
    // ...
    @Override
    public void execute( Environment env ) throws RuntimeError
    {
        Value vi = initial.evaluate(env);
        Value vb = limit.evaluate(env);
        Value sp = new Value(1);
        if( step != null )
            sp = step.evaluate(env);

        double prv = vi.real;
        env.update(param, vi);
        while( prv <=  vb.real ) {
            body.execute(env);
            prv += sp.real;
            env.update(param, new Value(prv));
        }
    }
    // ...
}
````

Նախապայմանով ցիկլի մոդել `While` դասի `execute()` մեթոդը շատ ավելի պարզ է։ Քանի
դեռ ցիկլի պայմանի հաշվարկված արժեքը հավասար չէ զրոյի, հաշվարկվում է ցիկլի մարմինը։

````java
public class While implements Statement {
    // ...
    @Override
    public void execute( Environment env ) throws RuntimeError
    {
        Value cond = condition.evaluate(env);
        while( cond.real != 0.0 ) {
            body.execute(env);
            cond = condition.evaluate(env);
        }
    }
    // ...
}
````


#### Պրոցեդուրայի կանչ


#### Հրամանների հաջորդում


