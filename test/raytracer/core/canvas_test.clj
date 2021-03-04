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