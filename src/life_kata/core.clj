(ns life-kata.core
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [life-kata.life :as l])
  (:gen-class))


(def ^:const SCREEN_WIDTH  500)
(def ^:const SCREEN_HEIGHT 500)


(defn setup! []

  (q/frame-rate 5)

  (l/random-arena 80 80 0.2))


(defn update-state [state]
  (l/next-arena state))


(defn draw-state! [state]

  (q/background 0 0 0)
  (q/fill 255 255 255)

  (let [{:keys [width height]} state]

    (let [block-width (/ SCREEN_WIDTH width)
          block-height (/ SCREEN_HEIGHT height)]
      
      (doseq [x (range 0 width)
              y (range 0 height)
              :when (l/alive? state x y)]

        (q/rect (* x block-width)
                (* y block-height)
                block-width block-height)))))


(defn -main [& args]
  (q/sketch :title "You spin my circle right round"
            :size [SCREEN_WIDTH SCREEN_HEIGHT]
            :setup setup!
            :update update-state
            :draw draw-state!
            :features [:keep-on-top]
            :middleware [m/fun-mode]))
