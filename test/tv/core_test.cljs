(ns tv.core-test
  (:require [cljsjs.moment]
            [cljs.test :refer [deftest is testing]]
            [tv.utils :refer [get-end-time has-finished?]]))

(def shows [{:finished {:duration  "00:30:00"
                        :startTime "2015-03-01 09:00:00"}}
            {:running  {:duration  "00:30:00"
                        :startTime (js/moment)}}])

(deftest test-has-finished? 
  (testing "TV Show"
    (testing "has finished"
      (is (has-finished? (:finished shows))))
    (testing "has not finished"
      (is (not (has-finished? (:running shows)))))))
