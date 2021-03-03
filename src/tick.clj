(ns tick
  (:require [tuple]))

(defn make-projectile
  "Makes a projectile from a given position point and velocity vector."
  [position velocity]
  {:position position :velocity velocity})

(defn make-environment
  "Makes an environment from a given gravity and wind vector."
  [gravity wind]
  {:gravity gravity :wind wind})

(defn tick
  "Advances the time by one step, returning a new projectile with
  an updated position and velocity given the effects of the
  environment."
  [projectile environment]
  (make-projectile
    (tuple/add (:position projectile) (:velocity projectile))
    (tuple/add (:velocity projectile) (:gravity environment) (:wind environment))))

(defn run
  "Executes the simulation with a default velocity-multiplier of
  2.0."
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