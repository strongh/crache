(ns crache.memo
  (:import [clojure.core.memoize PluggableMemoization])
  (:require [crache.cache :refer [redis-cache-factory]]
            [clojure.core.memoize :refer [build-memoizer]]))


(defn memo-redis
  [f conn key-prefix & [ttl]]
  (build-memoizer
   #(clojure.core.memoize.PluggableMemoization.
     %1 (apply redis-cache-factory conn key-prefix (when ttl [ttl])))
   f))
