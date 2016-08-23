package parser;

/**/
public enum Token {
    Unknown,
    Number,
    Identifier,

    Declare,
    Function,
    End,
    Input,
    Print,
    If,
    Then,
    ElseIf,
    Else,
    For,
    To,
    Step,
    While,
    Call,

    LeftParen,
    RightParen,
    Comma,

    Or,
    And,
    Not,

    Eq,
    Ne,

    Gt,
    Ge,
    Lt,
    Le,

    Add,
    Sub,
    Mul,
    Div,
    Power,

    NewLine,

    Eos
}
