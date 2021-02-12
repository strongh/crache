(ns crache.memo
  (:import [clojure.core.memoize PluggableMemoization])
  (:use [crache.cache :only [redis-cache-factory]]
        [clojure.core.memoize :only [build-memoizer]]))


(defn memo-redis
  [f conn key-prefix & [ttl]]
  (build-memoizer
   #(clojure.core.memoize.PluggableMemoization.
     %1 (apply redis-cache-factory conn key-prefix (when ttl [ttl])))
   f))
