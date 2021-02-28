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

(deftest make-vector-test
  (testing "when a new vector tuple is made"
    (is (= [4.3 -4.2 3.1 0.0]
           (SUT/make-vector 4.3 -4.2 3.1)))))

(deftest float-eq?-test
  (testing "when two floats with a difference smaller than EPSILON are compared"
    (is (= true (SUT/float-eq? 0.000001 0.000002))))
  (testing "when two floats with a difference larger than EPSILON are compared"
    (is (= false (SUT/float-eq? 0.00001 0.0002))))
  (testing "when two floats with a difference equal to EPSILON are compared"
    (is (= false (SUT/float-eq? 0.00001 0.00002)))))

(deftest tuple-eq?-test
  (testing "when two tuples with differences smaller than EPSILON are compared"
    (is (= true
           (SUT/tuple-eq?
             (SUT/make-tuple 0.000001 0.000001 0.000001 0.0)
             (SUT/make-tuple 0.000002 0.000002 0.000002 0.0)))))
  (testing "when two tuples with differences larger than EPSILON are compared"
    (is (= false
           (SUT/tuple-eq?
             (SUT/make-tuple 0.000001 0.000001 0.000001 0.0)
             (SUT/make-tuple 0.00002 0.00002 0.00002 0.0)))))
  (testing "when two tuples with differences equal to EPSILON are compared"
    (is (= false
           (SUT/tuple-eq?
             (SUT/make-tuple 0.00001 0.00001 0.00001 0.0)
             (SUT/make-tuple 0.00002 0.00002 0.00002 0.0))))))

(deftest add-test
  (testing "when two vectors are added"
    (is (= [10 0 0 0.0]
           (SUT/add (SUT/make-vector 5 -2 3)
                    (SUT/make-vector 5 2 -3)))))
  (testing "when a vector and a point are added"
    (is (= [1 1 6 1.0]
           (SUT/add (SUT/make-point 3 -2 5)
                    (SUT/make-vector -2 3 1))))))