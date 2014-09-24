(ns crache.cache
  (:require [clj-redis.client :as redis]
            [clojure.core.cache :as cache]))

(def CHARS
     (map char (concat (range 48 58)
                       (range 66 91)
                       (range 97 123))))

(defn gen-rand-str
  []
  (apply
   str
   (take 20 (repeatedly #(rand-nth CHARS)))))

(defn read-redis-str
  ([r kp item]
     (read-redis-str r kp item nil))
  ([r kp item not-found]
     (if-let [item-str (redis/get r (str kp (pr-str item)))]
       (read-string item-str)
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
        (redis/exists (:redis-connection $) (str (:key-prefix $) (pr-str item))))
  
  (hit [_ item]
       (RedisCache. $))
  
  (miss [_ item val]
        (redis/set (:redis-connection $)
                   (str (:key-prefix $) (pr-str item))
                   (pr-str (if (delay? val) @val val)))
        (RedisCache. $))
  
  (evict [_ item]
         (redis/del (:redis-connection $) (str (:key-prefix $) (pr-str item)))
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
        (redis/exists (:redis-connection $) (str (:key-prefix $) (pr-str item))))
  
  (hit [_ item]
       (RedisTTLCache. $))
  
  (miss [_ item val]
        (let [redis-connection (:redis-connection $)
              redis-key (str (:key-prefix $) (pr-str item))]
          (redis/set redis-connection redis-key (pr-str (if (delay? val) @val val)))
          (redis/expire redis-connection redis-key (:ttl $))
          (RedisTTLCache. $)))
  
  (evict [_ item]
         (redis/del (:redis-connection $) (str (:key-prefix $) (pr-str item)))
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
  (cache/seed (RedisCache. {}) {:host host :port port :delay delay}))

(defn redis-ttl-cache-factory
  [ttl & {:keys [host port delay key-prefix]
          :or {host "localhost"
               port 6379}}]
  (cache/seed (RedisTTLCache. {}) {:host host :port port :delay delay :ttl ttl}))
