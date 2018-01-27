# pitApp

Solution for PIT (canvas control points in android).

Pit is a direct extension to ViewGroup.
It shows the x and y axis, five initial random points connected by edges according to their location along the x-axis, and an add buttonץ New points are added at (0,0).

Each point is of class PointView, extending View.
It is given coordinates on the screen, assigned an onTouchListener, and added to the Point sorted List.
The PointView has a custom onTouchListener which handles the movement of the point on the screen, by updating its coordinates and setting its location on screen using built in setLeft, setRight, setTop, setBottom methods.
Every PointView draws a small circle of type ShapeDrawable within its borders using onDraw method.
Every change on screen (movement or point addition) resolves in ordering of the points list using Collection.sort(list) method, and the implemented Comparable interface.
The comparison is done by overriding compareTo(object), in which the x coordinate is evaluated.

Note:
  Sorting the array might be memory consuming. There could be anoter solution to sort the list while a point is moving.

Enjoy!
