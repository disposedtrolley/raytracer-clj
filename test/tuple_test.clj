(ns tuple-test
  (:require [clojure.test :refer [are deftest is testing]]
            [tuple :as SUT]))

(deftest tuple-parts-test
  (def my-tuple [4.3 -4.2 3.1 1.0])
  (testing "when parts of a tuple are accessed"
    (is (= 4.3 (SUT/x my-tuple)))
    (is (= -4.2 (SUT/y my-tuple)))
    (is (= 3.1 (SUT/z my-tuple)))
    (is (= 1.0 (SUT/w my-tuple)))))

(deftest make-point-test
  (testing "when a new point tuple is made"
    (is (= [4.3 -4.2 3.1 1.0]
           (SUT/make-point 4.3 -4.2 3.1)))))