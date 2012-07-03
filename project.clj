(defproject crache "1.0.0-SNAPSHOT"
  :description "Redis-backed caching and memoization, following core.cache and core.memoize"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.clojure/core.cache "0.6.0"]
                 [org.clojure/core.memoize "0.5.1"]
                 [org.clojars.strongh/clj-redis "0.0.20"]])