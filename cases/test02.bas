
DECLARE FUNCTION Sqr(x)

' Quadratic equation
FUNCTION Quadratic(a, b, c)
  d = b^2 - 4*a*c
  IF d >= 0 THEN
    sd = Sqr(d)
    x1 = (-b - sd) / (2*a)
    x2 = (-b + sd) / (2*a)
    PRINT x1
    PRINT x2
  END IF
END FUNCTION

FUNCTION Sqr(x)
  res = 1
  FOR i = 0 TO 100
    res = (res + x/res)/2
  END FOR
  Sqr = res
END FUNCTION

FUNCTION Main()
  CALL Quadratic 1, 4, 3
END FUNCTION
