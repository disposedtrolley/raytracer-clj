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

(defn is-vector?
  [t]
  (= 0.0 (w t)))

(defn is-point?
  [t]
  (= 1.0 (w t)))

(def EPSILON 0.00001)

(defn float-eq?
  [a b]
  (< (math/abs (- a b)) EPSILON))

(defn tuple-eq?
  [a b]
  (every? true? (map float-eq? a b)))

(defn add
  [a b]
  (if (and (is-point? a) (is-point? b))
    (throw (ex-info "Attempted to add two points" {:a a :b b})))
  (map + a b))

(defn sub
  [a b]
  (map - a b))

(defn neg
  [t]
  (sub (make-tuple 0 0 0 0)
       t))

(defn mul
  [t s]
  (map #(* % s) t))

(defn div
  [t s]
  (map #(/ % (float s)) t))

(defn mag
  [t]
  (if (not (is-vector? t))
    (throw (ex-info "Attempted to compute the magnitude of a non-vector tuple" {:t t})))
  (math/sqrt (reduce + (map #(math/expt % 2) t))))

(defn unit-vector?
  [t]
  (= 1.0 (mag t)))

(defn normalise
  [t]
  (if (not (is-vector? t))
    (throw (ex-info "Attempted to normalise a non-vector tuple" {:t t})))
  (map #(/ % (mag t)) t))

(defn dot
  [a b]
  (if (or (is-point? a) (is-point? b))
    (throw (ex-info "Attempted to compute dot product of a point tuple" {:a a :b b})))
  (reduce + (map * a b)))