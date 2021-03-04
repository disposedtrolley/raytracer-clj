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