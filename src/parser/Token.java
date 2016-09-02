package parser;

/**/
public enum Token {
    Unknown,
    Number,
    Text,
    Identifier,

    Declare,
    Function,
    End,
    Dim,
    Let,
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
