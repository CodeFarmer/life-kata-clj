(ns life-kata.unbounded-life)


;; arena is a map where the keys are row numbers that have active
;; cells, and the values are sets of column numbers identifying active
;; cells in that row
;; NOTE that empty rows should be removed, rather than left as empty sets

(defn empty-arena []
  {})


(defn full-arena [w h]
  (reduce (fn [amap x]
            (assoc amap x (into #{} (range w)))) {}
          (range h)))


(defn alive? [arena x y]
  (let [row (get arena y)]
    (if row
      (contains? row x)
      false)))


(defn arena-from-strings [aseq]
  (reduce (fn [a i]
            (let [row (get aseq i)
                  row' (into #{}
                           (filter #(not (= \. (get row %)))
                                   (range (count row))))]
              (if (empty? row') a
                  (assoc a i row'))))
          {}
          (range (count aseq))))


(defn with-alive [a x y]
  (let [row (get a y #{})]
    (assoc a y (conj row x))))


(defn with-dead [a x y]
  (let [row (get a y)
        row' (disj row x)]
    (if (empty? row')
      (dissoc a y)
      (assoc a y row'))))


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
  (for [y (keys arena)
        x (get arena y)]
    [x y]))

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
          {}
          (-interesting-cells arena)))
