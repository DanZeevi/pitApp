# pitApp

Solution for PIT (canvas control points in android).

Pit is a direct extension to ViewGroup.
It shows the x and y axis, five initial random points connected by edges according to their location along the x-axis, and an add point button. New points are added at (0,0).

Each point is of class PointView, extending View.
It is given coordinates on the screen, assigned an onTouchListener, and added to the Point sorted List.
The PointView has a custom onTouchListener which handles the movement of the point on the screen, by updating its coordinates and setting its location on screen using built in setLeft, setRight, setTop, setBottom methods.
Every PointView draws a small circle of type ShapeDrawable within its borders using onDraw method.
Every change on screen (movement or point addition) resolves in ordering of the points list using Collection.sort(list) method, and the implemented Comparable interface.
The comparison is done by overriding compareTo(object), in which the x coordinate is evaluated.

Addition:
Sorting the point list upon every movement (as implemented by TouchListener class) is redundant.
The EfficientTouchListener will sort the list only if necessary, using isSorted() method, checking if the movement made requires for the list to be sorted. Log calls will show these changes.
Note: The list will be sorted ANYWAY  upon point addition.

Enjoy!
