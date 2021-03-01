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

(deftest sub-test
  (testing "subtracting two points should produce a vector"
    (is (= (SUT/make-vector -2 -4 -6)
           (SUT/sub (SUT/make-point 3 2 1)
                    (SUT/make-point 5 6 7)))))
  (testing "subtracting a vector from a point should produce a point"
    (is (= (SUT/make-point -2 -4 -6)
           (SUT/sub (SUT/make-point 3 2 1)
                    (SUT/make-vector 5 6 7)))))
  (testing "subtracting two vectors should produce a vector"
    (is (= (SUT/make-vector -2 -4 -6)
           (SUT/sub (SUT/make-vector 3 2 1)
                    (SUT/make-vector 5 6 7))))))

(deftest neg-test
  (testing "when a vector is negated"
    (is (= (SUT/make-vector -1 2 -3)
           (SUT/neg (SUT/make-vector 1 -2 3))))))

(deftest mul-test
  (testing "multiplying a tuple by a scalar"
    (is (= (SUT/make-tuple 3.5 -7.0 10.5 -14.0)
           (SUT/mul (SUT/make-tuple 1 -2 3 -4) 3.5))))
  (testing "multiplying a tuple by a fraction scalar"
    (is (= (SUT/make-tuple 0.5 -1.0 1.5 -2.0)
           (SUT/mul (SUT/make-tuple 1 -2 3 -4) 0.5)))))