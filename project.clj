(defproject crache "1.0.0-SNAPSHOT"
  :description "Redis-backed caching and memoization, following core.cache and core.memoize"
  :license {:name "Eclipse Public License" :url "http://www.eclipse.org/legal/epl-v10.html"}
  :url "http://github.com/strongh/crache"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.clojure/core.cache "0.6.4"]
                 [org.clojure/core.memoize "0.5.1"]
                 [org.clojars.strongh/clj-redis "0.0.20"]
                 [com.taoensso/nippy "2.7.0-RC1"]])
