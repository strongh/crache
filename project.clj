(defproject crache "1.0.0-rc1-EMPEAR"
  :description "Redis-backed caching and memoization, following core.cache and core.memoize"
  :license {:name "Eclipse Public License" :url "http://www.eclipse.org/legal/epl-v10.html"}
  :url "http://github.com/strongh/crache"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [com.taoensso/carmine "2.19.1"]
                 [org.clojure/core.cache "0.7.2"]
                 [org.clojure/core.memoize "0.7.1"]])
