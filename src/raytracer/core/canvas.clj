(ns raytracer.core.canvas
  (:require [raytracer.core.tuples :as tuples]
            [clojure.string :as str]
            [clojure.math.numeric-tower :as math]))

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

(defn ^:private ppm-header
  "Returns a vector representing the lines in the PPM header."
  [width height depth]
  ["P3"
   (format "%d %d" width height)
   depth])

(defn ^:private scale-pixel-channel
  "Returns the pixel channel as a scaled value between 0 and 255.
  Output value is clamped if outside of the 0-255 range."
  [pixel]
  (let [pixel-min 0
        pixel-max 1
        output-min 0
        output-max 255]
    ;; Clamp values if necessary.
    (cond
      (< pixel pixel-min) output-min
      (> pixel pixel-max) output-max
      :else
      ;; Scale the pixel value.
      ;; https://stats.stackexchange.com/a/281165
      (+ (* (- output-max output-min)
            (/ (- pixel pixel-min)
               (- pixel-max pixel-min)))
         output-min))))

(defn ^:private pixel-to-str
  "Returns the pixel as a string of three numbers representing
  the red, green, and blue channels. Numbers are space-separated
  and range from 0 to 255."
  [pixel]
  ;; Yes, juxt can probably be used here but this is much clearer.
  (format "%d %d %d" (int (scale-pixel-channel (tuples/red pixel)))
                     (int (scale-pixel-channel (tuples/green pixel)))
                     (int (scale-pixel-channel (tuples/blue pixel)))))

(defn ^:private pixel-to-rgb
  [pixel]
  ((juxt tuples/red tuples/green tuples/blue) pixel))

(defn to-ppm
  "Renders the canvas as a PPM file, returning the file contents
  as a string."
  [canvas]
  (let [width (width canvas)
        height (height canvas)
        depth 255
        pixels (pixels canvas)
        terminator "\n"]
    (str (str/join terminator
                   (concat
                     (ppm-header width height depth)
                     (->> pixels
                          (map pixel-to-rgb)
                          (flatten)
                          (map #(int (scale-pixel-channel %)))
                          (partition (* 3 width) (* 3 width) nil)
                          (map #(str/join " " %)))))
         terminator)))