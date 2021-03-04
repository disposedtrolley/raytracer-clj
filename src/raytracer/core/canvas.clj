(ns raytracer.core.canvas)

(defn width
  [canvas]
  (:width canvas))

(defn height
  [canvas]
  (:height canvas))

(defn make-canvas
  "Makes a canvas object with the provided width and
  height, prefilled with black pixels."
  [width height]
  ;; One-dimensional array because it's simpler. We can always
  ;; make a custom iterator to return each row of pixels according
  ;; to the width of the image.
  {:pixels (vec (repeat (* width height)
                        (raytracer.core.tuples/make-colour 0 0 0)))
   :width width
   :height height})