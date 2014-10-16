(ns crache.memo-test
  (:require [clojure.test :refer :all]
            [crache.memo :refer :all]))

(defonce conn
  {:pool {}
   :spec {:host (System/getenv "DB_PORT_6379_TCP_ADDR")
          :port (Integer/parseInt (System/getenv "DB_PORT_6379_TCP_PORT"))}})

(deftest a-test
  (testing "Memoize a function"
    (let [f (memo-redis (fn [x] (System/currentTimeMillis)) conn (str ::memo) 5)
          v (f 10)]
      (is (= v (f 10)))
      (is (= v (f 10)))
      (Thread/sleep 5500)
      (let [v' (f 10)]
        (is (not= v v'))
        (is (= v' (f 10)))))))
