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
պահելու համար։ `Environment` դասը ես իրականացրել եմ որպես `String`-`Constant`
զույգերի ցուցակ։ Դրանում մեթոդներ են նախատեսված նոր փոփոխան ներմուծելու (`add`), 
գոյություն ունեցած փոփխականին նոր արժեք կապելու (`update`) և փոփոխականի արժեքը 
վերցնելու համար (`get`)։


### Արտահայտությունների մոդելները

Բեյսիկ֊Փ լեզվի արտահայտություններն իրականացնում են `Expression` ինտերֆեյսը։ 

````
public interface Expression {
    Constant evaluate( Environment env ) throws RuntimeError;
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






