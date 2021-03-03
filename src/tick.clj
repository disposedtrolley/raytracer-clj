(ns tick
  (:require [tuple]))

(defn make-projectile
  [position velocity]
  {:position position :velocity velocity})

(defn make-environment
  [gravity wind]
  {:gravity gravity :wind wind})

(defn tick
  [projectile environment]
  (make-projectile
    (tuple/add (:position projectile) (:velocity projectile))
    (tuple/add (tuple/add (:velocity projectile) (:gravity environment)) (:wind environment))))

(defn run
  ([]
   (run 2.0))
  ([velocity-multiplier]
   ((def p (make-projectile
             (tuple/make-point 0 1 0)
             (tuple/mul (tuple/normalise (tuple/make-vector 1 1 0)) velocity-multiplier)))
    (def e (make-environment
             (tuple/make-vector 0 -0.1 0)
             (tuple/make-vector -0.01 0 0)))

    (loop [p p
           i 0]
      (when (> (tuple/y (:position p)) 0)
       (printf "Tick: %d Position: %s\n" i p)
       (recur (tick p e) (inc i)))))))