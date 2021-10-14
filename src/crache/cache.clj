(ns crache.cache
  (:require [taoensso.carmine :as car]
            [clojure.core.cache :as cache]))

(defmacro wcar* [$ & body]
  `(car/wcar (:conn ~$) ~@body))

(defn key-for [$ item]
  (str (:key-prefix $) ":" (pr-str item)))

(cache/defcache RedisCache [$]
  cache/CacheProtocol
  (lookup [_ item]
    (delay (wcar* $ (car/get (key-for $ item)))))

  (lookup [_ item not-found]
    (delay (or (wcar* $ (car/get (key-for $ item))) not-found)))

  (has? [_ item]
    (= 1 (wcar* $ (car/exists (key-for $ item)))))

  (hit [_ item]
    (RedisCache. $))

  (miss [_ item val]
    (let [args (when-let [t (:ttl $)] ["ex" t])
          val (if (delay? val) @val val)]
      (wcar* $ (apply car/set (key-for $ item) (car/freeze (val)) args)))
    (RedisCache. $))

  (evict [_ item]
    (wcar* $ (car/del (key-for $ item)))
    (RedisCache. $))

  (seed [_ base]
    (RedisCache. base))

  Object
  (toString [_] (str $)))

(defn redis-cache-factory
  [conn key-prefix & [ttl]]
  (cache/seed (RedisCache. {}) {:conn conn :key-prefix key-prefix :ttl ttl}))
