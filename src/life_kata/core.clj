(ns life-kata.core
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [life-kata.unbounded-life :as l])
  (:gen-class))


(def ^:const SCREEN_WIDTH  500)
(def ^:const SCREEN_HEIGHT 500)


(defn setup! []

  (q/frame-rate 5)

  {:arena (l/random-arena 80 80 0.2)
   :port-left   -20
   :port-right  100
   :port-top    -20
   :port-bottom 100})


(defn update-state [state]
  (update-in state [:arena] l/next-arena))


(defn draw-state! [state]

  (q/background 0 0 0)
  (q/fill 255 255 255)

  (let ;; [{:keys [width height]} (:arena state)]
      
      [{:keys [arena port-left port-right port-top port-bottom]} state
       width  (- port-right port-left)
       height (- port-bottom port-top)]

    (let [block-width (/ SCREEN_WIDTH width)
          block-height (/ SCREEN_HEIGHT height)]

      ;; (println [port-left port-right port-top port-bottom] [width height] [block-width block-height])
      
      (doseq [[x y] (l/live-cells arena)]

        (q/rect (* (- x port-left) block-width)
                (* (- y port-top)  block-height)
                block-width block-height)))))


(defn -main [& args]
  (q/sketch :title "Life finds a way"
            :size [SCREEN_WIDTH SCREEN_HEIGHT]
            :setup setup!
            :update update-state
            :draw draw-state!
            :features [:keep-on-top]
            :middleware [m/fun-mode]))
