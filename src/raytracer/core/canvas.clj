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

(defn fill
  "Fills the entire canvas with the provided colour."
  [canvas colour]
  (assoc canvas :pixels
                (vec (repeat (* (width canvas) (height canvas))
                             colour))))

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

(defn ^:private colour-to-rgb
  "Returns the colour tuple as a three-element RGB tuple,
  effectively the same tuple less the w component."
  [colour]
  ((juxt tuples/red tuples/green tuples/blue) colour))

(defn ^:private ppm-partitioner
  "Partitions a list of pixels to conform to the PPM
  standard. Each partition of values will be the size
  of the image width provided or 70 characters, whichever
  is lower."
  [width]
  ;; I really, really don't like this function, but it passes the
  ;; unit tests in the book and I _think_ the logic is sound.
  ;;
  ;; Specifically, the magic number used for the space separation
  ;; padding (1) and the one used to compensate for the lack of a
  ;; space at the very end of a line (-2) bother me very much.
  ;;
  ;; There's likely a better way to compute the amount of space the
  ;; current val will take in the output string and make a decision
  ;; to partition.
  ;;
  ;; What I _do_ like is that I've managed to cut down on the amount
  ;; of state to two atoms (remaining-chars and remaining-vals-in-row)
  ;; from the four during the first cut. The cond statement is also
  ;; fairly clear in what it tries to achieve; first check if we've
  ;; processed all pixel channels for pixels in the current row, if
  ;; not check if we're about to exhaust the 70 character limit.
  (let [max-chars 70 ;; PPM file spec limit
        remaining-chars (atom max-chars)
        vals-per-pixel 3 ;; How many pixel channels in a single pixel.
        remaining-vals-in-row (atom (* width vals-per-pixel))] ;; How many pixel channel vals left in the current row.
    (fn [val]
      (let [val-char-len (+ (count (str val)) 1)] ;; Extra 1 for the space separator.
        (cond
          ;; All pixel channels in the row have been seen; time to
          ;; partition the input.
          (= 0 @remaining-vals-in-row)
          (do
            (reset! remaining-chars max-chars)
            (reset! remaining-vals-in-row (* width vals-per-pixel))
            val)
          ;; We're about to exhaust the 70 character line limit; time
          ;; to partition the input.
          (< (- @remaining-chars val-char-len) -2)
          (do
            (reset! remaining-chars max-chars)
            val)
          ;; Decrement remaining character count by length that val is
          ;; expected to occupy.
          ;; Decrement number of remaining pixel values for this row.
          :else
          (do
            (reset! remaining-chars (- @remaining-chars val-char-len))
            (swap! remaining-vals-in-row dec)
            nil))))))

(defn ^:private ppm-body
  [pixels width]
  (->> pixels
       (map colour-to-rgb)
       (flatten)
       (map #(int (scale-pixel-channel %)))
       (partition-by (ppm-partitioner width))
       (map #(str/join " " %))))

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
                     (ppm-body pixels width)))
         terminator)))