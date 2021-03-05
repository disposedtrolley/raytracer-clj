(ns raytracer.core.canvas-test
  (:require [clojure.test :refer :all]
            [raytracer.core.tuples :as tuples]
            [raytracer.core.canvas :as SUT]))

(deftest make-canvas-test
  (testing "creating a canvas"
    (let [test-canvas (SUT/make-canvas 10 20)]
      (is (= 10
             (SUT/width test-canvas)))
      (is (= 20
             (SUT/height test-canvas)))
      (is (= (tuples/make-colour 0 0 0)
             (get (:pixels test-canvas) 0)))
      (is (= true
             (tuples/is-colour? (get (:pixels test-canvas) 0)))))))

(deftest write-test
  (let [test-canvas (SUT/make-canvas 10 20)
        red (tuples/make-colour 1 0 0)]
    (testing "writing pixels to the canvas"
      (let [mutated-canvas (SUT/write test-canvas 2 3 red)]
        (is (= red
               (SUT/pixel mutated-canvas 2 3)))))
    (testing "writing pixels at the boundaries of the canvas"
      (let [mutated-canvas (SUT/write (SUT/write test-canvas 0 0 red) 9 19 red)]
        (is (= red
               (SUT/pixel mutated-canvas 0 0)))
        (is (= red
               (SUT/pixel mutated-canvas 9 19)))))))

(deftest to-ppm-test
  (let [c (SUT/make-canvas 5 3)
        c1 (tuples/make-colour 1.5 0 0)
        c2 (tuples/make-colour 0 0.5 0)
        c3 (tuples/make-colour -0.5 0 1)]
    (testing "writes the PPM file correctly"
      (let [c (SUT/write c 0 0 c1)
            c (SUT/write c 2 1 c2)
            c (SUT/write c 4 2 c3)]
        (is (= "P3
5 3
255
255 0 0 0 0 0 0 0 0 0 0 0 0 0 0
0 0 0 0 0 0 0 127 0 0 0 0 0 0 0
0 0 0 0 0 0 0 0 0 0 0 0 0 0 255"
               (SUT/to-ppm c)))))))