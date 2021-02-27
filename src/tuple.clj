(ns tuple
  (:require [clojure.math.numeric-tower :as math]))

(defn make-point [x y z]
  [x y z 1.0])

(defn make-vector [x y z]
  [x y z 0.0])

(def EPSILON (cast Double 0.00001))

(defn float-eq?
  [a b]

  (< (math/abs (- a b)) EPSILON))

(defn tuple-eq?
  [a b]
  (every? true? (map float-eq? a b)))