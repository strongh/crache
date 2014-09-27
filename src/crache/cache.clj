(ns crache.cache
  (:require [clj-redis.client :as redis]
            [clojure.core.cache :as cache]
            [taoensso.nippy :refer [freeze thaw]]))

(def CHARS
     (map char (concat (range 48 58)
                       (range 66 91)
                       (range 97 123))))

(defn gen-rand-str
  []
  (apply
   str
   (take 20 (repeatedly #(rand-nth CHARS)))))

(defn freeze-them [& liquids] (freeze liquids))

(defn read-redis-str
  ([r kp item]
     (read-redis-str r kp item nil))
  ([r kp item not-found]
     (if-let [item-val (redis/get r (freeze-them kp item))]
       (thaw item-val)
       not-found)))

(cache/defcache RedisCache [$]
  cache/CacheProtocol
  (lookup [_ item]
          (if (:delay $)
            (delay (read-redis-str
                    (:redis-connection $)
                    (:key-prefix $)
                    item))
            (read-redis-str
             (:redis-connection $)
             (:key-prefix $)
             item)))

  (lookup [_ item not-found]
          (if (:delay $)
            (delay (read-redis-str
                    (:redis-connection $)
                    (:key-prefix $)
                    item not-found))
            (read-redis-str
             (:redis-connection $)
             (:key-prefix $)
             item not-found)))
  
  (has? [_ item]
        (redis/exists (:redis-connection $) (freeze-them (:key-prefix $) item)))
  
  (hit [_ item]
       (RedisCache. $))
  
  (miss [_ item val]
        (redis/set (:redis-connection $)
                   (freeze-them (:key-prefix $) item)
                   (freeze (if (delay? val) @val val)))
        (RedisCache. $))
  
  (evict [_ item]
         (redis/del (:redis-connection $) (freeze-them (:key-prefix $) item))
         (RedisCache. $))
  
  (seed [_ base]
        (RedisCache.
         {:key-prefix (:key-prefix base (gen-rand-str))
          :delay (:delay base)
          :redis-connection (redis/init {:url (str "redis://" (:host base)
                                                   ":" (:port base))})}))
  Object
  (toString [_] (str $)))


(cache/defcache RedisTTLCache [$]
  cache/CacheProtocol
  (lookup [_ item]
          (if (:delay $)
            (delay (read-redis-str
                    (:redis-connection $)
                    (:key-prefix $)
                    item))
            (read-redis-str
             (:redis-connection $)
             (:key-prefix $)
             item)))

  (lookup [_ item not-found]
          (if (:delay $)
            (delay (read-redis-str
                    (:redis-connection $)
                    (:key-prefix $)
                    item not-found))
            (read-redis-str
             (:redis-connection $)
             (:key-prefix $)
             item not-found)))
  
  (has? [_ item]
        (redis/exists (:redis-connection $)
                      (freeze-them (:key-prefix $) item)))
  
  (hit [_ item]
       (RedisTTLCache. $))
  
  (miss [_ item val]
        (let [redis-connection (:redis-connection $)
              redis-key (freeze-them (:key-prefix $) item)]
          (redis/set redis-connection redis-key (freeze (if (delay? val) @val val)))
          (redis/expire redis-connection redis-key (:ttl $))
          (RedisTTLCache. $)))
  
  (evict [_ item]
         (redis/del (:redis-connection $) (freeze-them (:key-prefix $) item))
         (RedisTTLCache. $))
  
  (seed [_ base]
        (RedisTTLCache.
         {:key-prefix (:key-prefix base (gen-rand-str))
          :delay (:delay base)
          :ttl (long (:ttl base))
          :redis-connection (redis/init {:url (str "redis://" (:host base)
                                                   ":" (:port base))})}))
  Object
  (toString [_] (str $)))


(defn redis-cache-factory
  [& {:keys [host port delay key-prefix]
      :or {host "localhost"
           port 6379}}]
  (cache/seed (RedisCache. {}) {:host host :port port
                                :delay delay :key-prefix key-prefix}))

(defn redis-ttl-cache-factory
  [ttl & {:keys [host port delay key-prefix]
          :or {host "localhost"
               port 6379}}]
  (cache/seed (RedisTTLCache. {}) {:host host :port port
                                   :delay delay :ttl ttl :key-prefix key-prefix}))
