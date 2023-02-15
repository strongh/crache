(ns crache.memo
  (:require [crache.cache :refer [redis-cache-factory]]
            [clojure.core.memoize :refer [memoizer]]))

(defn memo-redis
  [f conn key-prefix & [ttl]]
  (memoizer
   f
   (apply redis-cache-factory conn key-prefix (when ttl [ttl]))))

(defn memo-ns
  "Memoizes all functions in given namespace"
  [target-ns source-ns conn]
  (let [publics (ns-publics source-ns)]

    (run!
     (fn [[sym var]]
       (println :intern target-ns sym)
       (intern target-ns sym
               (memo-redis (var-get var)
                           conn
                           (str
                            (ns-resolve
                             source-ns
                             sym)))))
     (map vector
          (keys publics)
          (vals publics)))))
