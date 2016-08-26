
' recursive
FUNCTION Rec(n)
  IF n = 1 THEN
    Rec = 1
  ELSE
    Rec = n + Rec(n - 1)
  END IF
END FUNCTION


FUNCTION Main()
  PRINT Rec(10.0)
  PRINT Rec(100)
END FUNCTION
