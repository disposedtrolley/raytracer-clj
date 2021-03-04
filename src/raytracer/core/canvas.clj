(ns raytracer.core.canvas
  (:require [raytracer.core.tuples :as tuples]))

(defn width
  [canvas]
  (:width canvas))

(defn height
  [canvas]
  (:height canvas))

(defn pixels
  [canvas]
  (:pixels canvas))

(defn make-canvas
  "Makes a canvas object with the provided width and
  height, prefilled with black pixels."
  [width height]
  ;; One-dimensional array because it's simpler. We can always
  ;; make a custom iterator to return each row of pixels according
  ;; to the width of the image.
  {:pixels (vec (repeat (* width height)
                        (tuples/make-colour 0 0 0)))
   :width width
   :height height})

(defn write
  "Writes a pixel to the canvas at the x and y coordinates
  provided, returning a new copy of the canvas."
  [canvas x y pixel]
  (if (or (>= x (width canvas)) (< x 0))
    (throw (ex-info "x should be between 0 and the canvas' width-1" {:canvas canvas :x x :y y :pixel pixel})))
  (if (or (>= y (height canvas)) (< y 0))
    (throw (ex-info "y should be between 0 and the canvas' width-1" {:canvas canvas :x x :y y :pixel pixel})))
  (let [pixel-idx (+ (* y (width canvas)) x)]
    (assoc canvas :pixels (assoc (pixels canvas) pixel-idx pixel))))

(defn pixel
  "Returns the pixel at the x and y coordinates of the canvas."
  [canvas x y]
  (let [pixel-idx (+ (* y (width canvas)) x)]
    (get (pixels canvas) pixel-idx)))
