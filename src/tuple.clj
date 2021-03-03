(ns tuple
  (:require [clojure.math.numeric-tower :as math]))

(defn x
  "Returns the X component of the tuple."
  [tuple]
  (get tuple 0))

(defn y
  "Returns the Y component of the tuple."
  [tuple]
  (get tuple 1))

(defn z
  "Returns the Z component of the tuple."
  [tuple]
  (get tuple 2))

(defn w
  "Returns the W component of the tuple."
  [tuple]
  (get tuple 3))

(defn red
  [colour]
  (get colour 0))

(defn green
  [colour]
  (get colour 1))

(defn blue
  [colour]
  (get colour 2))

(defn make-tuple [x y z w]
  "Makes a new 4-element tuple from x y z w."
  [x y z w])

(defn make-point [x y z]
  "Makes a new point tuple."
  (make-tuple x y z 1.0))

(defn make-vector [x y z]
  "Makes a new vector tuple."
  (make-tuple x y z 0.0))

(defn make-colour [r g b]
  (make-tuple r g b 1.0))

(defn is-vector?
  "Returns if the tuple t represents a vector."
  [t]
  (= 0.0 (w t)))

(defn is-point?
  "Returns if the tuple t represents a point."
  [t]
  (= 1.0 (w t)))

;; Constant to aid in floating point comparisons.
(def EPSILON 0.00001)

(defn float-eq?
  "Returns if the floats a and b are equal; i.e. their absolute
  difference is smaller than EPSILON."
  [a b]
  (< (math/abs (- a b)) EPSILON))

(defn tuple-eq?
  "Returns if all corresponding components of tuples a and b
  are equal by applying float-eq? to each pair."
  [a b]
  (every? true? (map float-eq? a b)))

(defn add
  "Returns the addition of tuples a and b as a new tuple."
  [a b]
  (if (and (is-point? a) (is-point? b))
    (throw (ex-info "Attempted to add two points" {:a a :b b})))
  (into [] (map + a b)))

(defn sub
  "Returns the subtraction of tuples a and b as a new tuple."
  [a b]
  (into [] (map - a b)))

(defn neg
  "Returns the negation of tuple t."
  [t]
  (sub (make-tuple 0 0 0 0)
       t))

(defn mul
  "Returns each component of tuple t multiplied by scalar s as
  a new tuple."
  [t s]
  (into [] (map #(* % s) t)))

(defn div
  "Returns each component of tuple t divided by scalar s as
  a new tuple."
  [t s]
  (into [] (map #(/ % (float s)) t)))

(defn mag
  "Returns the magnitude of a vector tuple t."
  [t]
  (if (not (is-vector? t))
    (throw (ex-info "Attempted to compute the magnitude of a non-vector tuple" {:t t})))
  (math/sqrt (reduce + (map #(math/expt % 2) t))))

(defn unit-vector?
  "Returns whether the tuple t is a unit vector."
  [t]
  (= 1.0 (mag t)))

(defn normalise
  "Returns the normalised form of the vector tuple t."
  [t]
  (if (not (is-vector? t))
    (throw (ex-info "Attempted to normalise a non-vector tuple" {:t t})))
  (into [] (map #(/ % (mag t)) t)))

(defn dot
  "Returns the dot product of the vector tuple t."
  [a b]
  (if (or (is-point? a) (is-point? b))
    (throw (ex-info "Attempted to compute dot product of a point tuple" {:a a :b b})))
  (reduce + (map * a b)))

(defn cross
  "Returns the cross product of the vector tuple t."
  [a b]
  (if (or (is-point? a) (is-point? b))
    (throw (ex-info "Attempted to compute cross product of a point tuple" {:a a :b b})))
  (make-vector
    (- (* (y a) (z b)) (* (z a) (y b)))
    (- (* (z a) (x b)) (* (x a) (z b)))
    (- (* (x a) (y b)) (* (y a) (x b)))))