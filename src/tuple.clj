(ns tuple
  (:require [clojure.math.numeric-tower :as math]))

(defn x
  [tuple]
  (get tuple 0))

(defn y
  [tuple]
  (get tuple 1))

(defn z
  [tuple]
  (get tuple 2))

(defn w
  [tuple]
  (get tuple 3))

(defn make-tuple [x y z w]
  [x y z w])

(defn make-point [x y z]
  (make-tuple x y z 1.0))

(defn make-vector [x y z]
  (make-tuple x y z 0.0))

(def EPSILON (cast Double 0.00001))

(defn float-eq?
  [a b]

  (< (math/abs (- a b)) EPSILON))

(defn tuple-eq?
  [a b]
  (every? true? (map float-eq? a b)))