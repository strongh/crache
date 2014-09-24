(ns crache.memo
  (:import [clojure.core.memoize.PluggableMemoization])
  (:use [crache.cache :only [redis-cache-factory redis-ttl-cache-factory]]
        [clojure.core.memoize :only [build-memoizer]]))


(defn memo-redis
  [f & {:keys [host port key-prefix]
        :or {host "localhost"
             port 6379}}]
  (build-memoizer
   #(clojure.core.memoize.PluggableMemoization.
     %1 (redis-cache-factory :host host :port port :delay true :key-prefix key-prefix))
   f))

(defn memo-ttl-redis
  [f ttl & {:keys [host port key-prefix]
            :or {host "localhost"
                 port 6379}}]
  (build-memoizer
   #(clojure.core.memoize.PluggableMemoization.
     %1 (redis-ttl-cache-factory ttl :host host :port port
                                 :delay true :key-prefix key-prefix))
   f))
