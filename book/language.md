## Բեյսիկ-Փ լեզուն

Բեյսիկ-Փ լեզուն դասական BASIC ընտանիքի լեզու է` պարզեցված ուսուցողական և 
ցուցադրական նպատակների համար։ BASIC-ից ես ընտրել եմ միայն այն ղեկավարող 
կառուցվածքներն ու արտահայտությունների տեսակները, որոնք յուրահատուկ են 
համարյա բոլոր պրոցեդուրային, ստրուկտուրավորված լեզուներին։ 

Ստորև հատված առ հատված ներկայացնում եմ Բեյսիկ-Փ լեզվի շարահյուսությունը՝ 
EBNF գրառմամբ՝ անհրաժեշտության դեպքում լրացված մանրամասն բացատրություններով։ 

### Ֆունկցիայի հայտարարում և սահմանում

Բեյսիկ-Փ լեզվով գրված ծրագիրը ֆունկցիաների հայտարությունների ու սահմանումների 
հաջորդականություն է.

````
Program = { (Declare | Function) }.
````

Հայտարարությունը սկսվում է `DECLARE` ծառայողական բառով, որին հաջորդում է 
ֆունկցիայի վերնագիրը.

````
Declare = 'DECLARE' FuncHeader.
````

Օրինակ՝ `DECLARE FUNCTION max(x,y)` տողը հայտարարում է `x` և `y` պարամետրերով
`max` ֆունկցիան։

Ֆունկցիայի վերնագիրը սկսվում է `FUNCTION` ծառայողական բառով, որին հետևում է 
ֆունկցիայի անունը ցույց տվող իդենտիֆիկատորը, այնուհետև՝ `(` և `)` փակագծերի 
մեջ առված պարամետրերի ցուցակը։ Պարամետրերի ցուցակը ստորակետով իրարից 
բաժանված իդենտիֆիկատորների հաջորդականություն է, որ կարող է նաև դատարկ լինել։
Վերնագիրը անպայման պետք է ավարտվի գոնե մեկ նոր տողի նիշով։

````
FuncHeader = 'FUNCTION' IDENT '(' [IDENT {',' IDENT}] ')' NewLines.
````

Ֆունկցիայի սահմանումը սկսվում է ֆունկցիայի վերնագրով, որին հետևում է հրամանների 
հաջորդականությունը, այնուհետև՝ `END` և `FUNCTION` ծառայողական բառերը։

````
Function = FuncHeader { Statement NewLines } 'END' 'FUNCTION'.
````

### Հրամաններ 

Բեյսիկ-Փ լեզվի հրամանները վեցն են. _վերագրում_, _ներածում_, _արտածում_, 
_ճյուղավորում_, _պարամետրով_ և _նախապայմանով_ ցիկլեր։ 

Վերագրման հրամանը սկսվում է վերագրվող փոփոխականը որոծող իդենտիֆիկատորով,
որին հետևում է `=` նիշը, այնուհետև՝ վերագրվող արտահայտությունը։ 

````
Statement = IDENT '=' Disjunction
    | INPUT IDENT
    | PRINT Disjunction
    | IF Disjunction THEN NewLines { Statement NewLines }
      { ELSEIF Disjunction THEN NewLines { Statement NewLines } }
      [ ELSE NewLines { Statement NewLines } ]
       END IF
    | FOR IDENT '=' Disjunction TO Disjunction 
      [STEP Disjunction] NewLines
      { Statement NewLines } END FOR
    | WHILE Disjunction NewLines { Statement } END WHILE.
````

### Արտահայտություններ

````
Disjunction = Conjunction { OR Conjunction }.
Conjunction = Equation { AND Aquation }.
Equation = Relation [ ('=' | '<>') Relation ].
Relation = Addition [ ('>' | '>=' | '<' | '<=') Addition ].
Addition = Multiplication { ('+' | '-') Multiplication }.
Multiplication = Power { ('*' | '/') Power }.
Power = Factor [ '^' Power ].
Factor = IDENT '(' [ Disjunction { ',' Disjunction } ] ')' 
    | '(' Disjunction ')'
    | NOT Factor
    | '-' Factor
    | IDENT
    | NUMBER.
````