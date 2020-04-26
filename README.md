# life-kata

A naive, fairly idiomatic Clojure implementation of Conway's Game of
Life made for a kata class run at Expedia.

The Game is rendered via a Quil sketch.

## Running

`lein run`

## Limitations

The code is designed to be Clojurey and show interesting language
features (the class was mostly done in Python and Java) rather than
run efficiently; updating the :cells member of the bounded arena
directly once per frame would certainly be a lot faster, for example!

## Next

- Combine the bounded and unbounded arena into a Clojure protocol
- Extract out the utility methods to be shared
- Allow the view port to pan and move
- Compare the speeds of the arena types when they get large
