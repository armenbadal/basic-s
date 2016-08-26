
FUNCTION Quadrant(x,y)
    IF x > 0 AND y > 0 THEN
        Quadrant = 1
    ELSEIF x < 0 AND y > 0 THEN
        Quadrant = 2
    ELSEIF x < 0 AND y < 0 THEN
        Quadrant = 3
    ELSEIF x > 0 AND y < 0 THEN
        Quadrant = 4
    END IF
END FUNCTION

FUNCTION Main()
  PRINT Quadrant(-3, 2)
END FUNCTION
