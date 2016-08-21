# «Բեյսիկ-Փ» ծրագրավորման լեզվի իրականացում։

Այս նախագծի ամբողջական և մանրամասն նկարագրությունը պետք է դառնա ինտերպրետատոր գրելու ուղեցույց։ Պրոյեկտի լրացումները ձևակերպվելու են որպես խնդիրներ՝ ինքնուրույն աշխատանքների համար։

Նկարագրության պլանը.

1. Ընդհանուր տեղեկություններ իրականացվող ինտերպրետատորի մասին,
2. Լեզվի շարահյուսությունը,
3. Ինտերպրետացիայի միջուկը. Function, Statement և Expression,
4. Շարահյուսական վերլուծություն և սխալների հայտնաբերում,
5. Կատարում (և տեստավորում):


## Շարահյուսություն

Բեյսիկ-Փ լեզվի շարահյուսությունը.

````
Program = { (Declare | Function) }.
Declare = DECLARE FuncHeader.
FuncHeader = FUNCTION IDENT '(' ')' NewLines.
Function = FuncHeader { Statement NewLines } END FUNCTION.
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
