(ns life-kata.unbounded-life-test
  (:require [clojure.test :refer :all]
            [life-kata.unbounded-life :refer :all]))


(deftest empty-arena-test
  
  (testing "empty arena is a thing"
    (is (not (nil? (empty-arena))))))


(deftest full-arena-test
  
  (testing "full arena is a thing"
    (is (not (nil? (full-arena 10 10)))))


  (testing "full arena only has alive cells"

    (let [awidth 10
          aheight 20
          fa (full-arena awidth aheight)]

      (is (every? true?
                  (for [x (range 0 awidth)
                        y (range 0 aheight)]
                    (alive? fa x y))))))

  (testing "cells outside the arena"

    (let [awidth 5
          aheight 5
          fa (full-arena awidth aheight)]

      (is (not (alive? fa 6 0))
          "x higher than width should not be alive")
      (is (not (alive? fa -1 5))
          "x less than zero should not be alive")

      (is (not (alive? fa 0 5))
          "y equal to height should not be alive")
      (is (not (alive? fa 0 -1))
          "x less than zero should not be alive"))))


(deftest string-arena-test

  (testing "empty arena creation from string seq"

    (is (= (empty-arena)
           (arena-from-strings ["..."
                                "..."
                                "..."]))))

  (testing "non-empty arena creation from string seq"

    (let [a (arena-from-strings ["X.."
                                 ".X."])]
      (is (alive? a 0 0) "top left cell should be alive")
      (is (not (alive? a 0 1)) "bottom left cell should not be alive")
      (is (alive? a 1 1) "middle cell of the bottom row should be alive"))))



(deftest updated-arena-test

  (testing "making cells alive"
    (is (alive? (with-alive (empty-arena)
                  0 0) 0 0))))


(deftest counting-neighbours-test
  (let [a (arena-from-strings ["XXX"
                               "..."
                               "XXX"])]
    
    (is (= 1 (-count-neighbours a 0 0)))
    (is (= 2 (-count-neighbours a 1 0)))
    (is (= 6 (-count-neighbours a 1 1)))))


(deftest cell-survival-test

  (testing "any live cell fewer than two neighbours dies"

    (let [a (arena-from-strings ["X."
                                 ".."])]
      (is (not (survives? a 0 0)))))
  
  (testing "any live cell with two or three live neighbours survives"
    
    (let [a (arena-from-strings ["XX"
                                 "X."])]
      (is (survives? a 0 0)))

    (let [a (arena-from-strings ["XXX"
                                 "X.."])]
      (is (survives? a 0 1))))

  (testing "any live cell with more than three live neighbours dies"

    (let [a (arena-from-strings ["XXX"
                                 "XX."])]
      (is (not (survives? a 1 0)))
      (is (not (survives? a 1 1)))))

  (testing "any dead cell with exactly three live neighbours becomes alive"

    (let [a (arena-from-strings ["XXX"
                                 "..."])]
      (is (not (survives? a 0 1)))
      (is (survives? a 1 1))
      (is (not (survives? a 2 1))))))


(deftest live-cells-test
  (testing "live-cells list is correct"
    (let [a (arena-from-strings ["XXX"
                                 "..."])]
      (is (= [[0 0] [1 0] [2 0]]
             (live-cells a))))))


(deftest interesting-cells-test
  (testing "interesting cells are live cells, and cells adjacent to live cells"
    (let [a (arena-from-strings ["X.."
                                 "..."
                                 "..."])]
      (is (= [[-1 -1] [-1 0] [-1 1] [0 -1] [0 0] [0 1] [1 -1] [1 0] [1 1]]
             (-interesting-cells a))))))


(deftest iteration-test

  (testing "still lifes"
    (let [block (arena-from-strings ["...."
                                     ".XX."
                                     ".XX."
                                     "...."])]
      (is (= block (next-arena block)))))

  (testing "oscillators"
    
    (let [vbar (arena-from-strings ["....."
                                    "..X.."
                                    "..X.."
                                    "..X.."
                                    "....."])
          hbar (arena-from-strings ["....."
                                    "....."
                                    ".XXX."
                                    "....."
                                    "....."])]
      (is (= hbar (next-arena vbar)))
      (is (= vbar (next-arena hbar))))

    (let [beacon0 (arena-from-strings ["......"
                                       ".XX..."
                                       ".X...."
                                       "....X."
                                       "...XX."
                                       "......"])
          beacon1 (arena-from-strings ["......"
                                       ".XX..."
                                       ".XX..."
                                       "...XX."
                                       "...XX."
                                       "......"])]
      (is (= beacon1 (next-arena beacon0)))
      (is (= beacon0 (next-arena beacon1))))))
  
  
  
  
  
  
  
  
