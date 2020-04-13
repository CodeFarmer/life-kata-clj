(ns life-kata.life)


(defn height [arena]
  (:height arena))

(defn width [arena]
  (:width arena))

(defn empty-arena [width height]
  {:width width
   :height height
   :cells (into [] (repeat (* width height) false))})

(defn full-arena [width height]
  {:width width
   :height height
   :cells (into [] (repeat (* width height) true))})


(defn random-arena [width height weight]
  {:width width
   :height height
   :cells (into [] (take (* width height)) (repeatedly #(< (rand) weight)))})

(defn -cell-index [arena x y]
  (+ x (* y (width arena))))

(defn alive? [arena x y]
  (if (or (>= x (width arena))
          (< x 0)
          (>= y (height arena))
          (< y 0)) false
    (get (:cells arena) (-cell-index arena x y))))


(defn arena-from-strings [aseq]
  "Create arenas from strings, mostly for visually understandable testing"
  (let [width (count (first aseq))
        height (count aseq)]
    {:width width
     :height height
     :cells 
     (into [] (map #(not (= \. %)) (apply concat aseq)))}))


(defn -with-liveness [a x y l]
  (assoc-in a [:cells (-cell-index a x y)] l))

(defn with-alive [a x y]
  "return an arena the same as a, but with the cell at [x y] alive"
  (-with-liveness a x y true))

(defn with-dead [a x y]
  "return an arena the same as a, but with the cell at [x y] dead"
  (-with-liveness a x y false))


(defn -count-neighbours [a x y]
  (count
   (filter true?
           (for [x' [-1 0 1]
                 y' [-1 0 1]
                 :when (not (= [0 0] [x' y']))]
             (alive? a (+ x x') (+ y y'))))))


(defn survives? [a x y]
  (let [alive (alive? a x y)
        nc (-count-neighbours a x y)]

    (cond
      (and alive (contains? #{2 3} nc)) true
      (and (not alive) (= nc 3)) true
      :default false)))

(defn next-arena [a]
  (reduce (fn [a [x y l]]
            (-with-liveness a x y l))
          a
          (for [x (range 0 (width a))
                y (range 0 (height a))]
            [x y (survives? a x y)])))
