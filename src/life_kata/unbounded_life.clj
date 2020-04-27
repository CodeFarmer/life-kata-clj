(ns life-kata.unbounded-life)


;; arena is a map where the keys are row numbers that have active
;; cells, and the values are sets of column numbers identifying active
;; cells in that row
;; NOTE that empty rows should be removed, rather than left as empty sets

(defn empty-arena []
  #{})


(defn full-arena [w h]
  (into #{} (for [x (range w)
                  y (range h)]
              [x y])))


(defn alive? [arena x y]
  (contains? arena [x y]))


(defn arena-from-strings [aseq]
  (into #{}
        (for [y (range (count aseq))
              x (range (count (get aseq y)))
              :when (not (= \. (get (get aseq y) x)))]
          [x y])))


(defn with-alive [a x y]
  (conj a [x y]))


(defn random-arena [width height weight]
  (reduce (fn [a [x y]]
            (if (< (rand) weight)
              (with-alive a x y)
              a))
          (empty-arena)
          (for [x (range width)
                y (range height)]
            [x y])))


;; FIXME this is common now
(defn -count-neighbours [a x y]
  (count
   (filter true?
           (for [x' [-1 0 1]
                 y' [-1 0 1]
                 :when (not (= [0 0] [x' y']))]
             (alive? a (+ x x') (+ y y'))))))


;; FIXME this is common now
(defn survives? [a x y]
  (let [alive (alive? a x y)
        nc (-count-neighbours a x y)]

    (cond
      (and alive (contains? #{2 3} nc)) true
      (and (not alive) (= nc 3)) true
      :default false)))

(defn live-cells [arena]
  (seq arena))

;; this is the observation that the only potentially alive cells in the next generation are within one space of a presently-alive cell
(defn -interesting-cells [arena]
  (distinct
   (for [[x y] (live-cells arena)
         x' [-1 0 1]
         y' [-1 0 1]]
     [(+ x x') (+ y y')])))

(defn next-arena [arena]
  (reduce (fn [a [x y]]
            (if (survives? arena x y)
              (with-alive a x y)
              a))
          (empty-arena)
          (-interesting-cells arena)))
