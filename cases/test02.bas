

DECLARE FUNCTION Abs(x)
DECLARE FUNCTION Sqr(x)

' Quadratic equation
FUNCTION Quadratic(a, b, c)
  d = b^2 - 4*a*c
  IF d >= 0 THEN
    x1 = (-b - Sqr(d)) / 2*a
    x2 = (-b + Sqr(d)) / 2*a
    PRINT x1
    PRINT x2
  END IF
END FUNCTION

FUNCTION Abs(x)
  Abs = x
  IF x < 0 THEN
    Abs = -x
  END IF
END FUNCTION

' TODO implement Newton's method
FUNCTION Sqr(x)
  Sqr = x
END FUNCTION

FUNCTION Main()
  CALL Quadratic 1, 12, 3
END FUNCTION
