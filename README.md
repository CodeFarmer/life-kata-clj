# life-kata

A naive, fairly idiomatic Clojure implementation of Conway's Game of
Life made for a kata class run at Expedia.

The Game is rendered via a Quil sketch.

## Running

`lein run`

## Limitations

The Game is supposed to be played in an infinite arena; making a data
structure that represents such a thing is not that hard, but figuring
out how to draw it was beyond the amount of time I had. This is
definitely what I'll do for the next iteration though.

A better arena will have no width or height interface, but be able to
give hints to a renderer as to what might be an interesting field to
draw.

The code is designed to be Clojurey and show interesting language
features (the class was mostly done in Python and Java) rather than
run efficiently; updating the :cells member of the arena directly once
per frame would certainly be a lot faster, for example!
