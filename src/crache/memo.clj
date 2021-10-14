(ns crache.memo
  (:require [crache.cache :refer [redis-cache-factory]]
            [clojure.core.memoize :refer [memoizer]]))

(defn memo-redis
  [f conn key-prefix $ [ttl]]
  (memoizer
   f
   (apply redis-cache-factory conn key-prefix (when ttl [ttl]))))
