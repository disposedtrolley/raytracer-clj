(ns tuple-test
  (:require [clojure.test :refer [are deftest is testing]]
            [tuple :as SUT]))

(deftest test-hello
  (is (= 1 1)))