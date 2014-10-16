(defproject crache "1.0.0-SNAPSHOT"
  :description "Redis-backed caching and memoization, following core.cache and core.memoize"
  :license {:name "Eclipse Public License" :url "http://www.eclipse.org/legal/epl-v10.html"}
  :url "http://github.com/strongh/crache"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [com.taoensso/carmine "2.7.0"]
                 [org.clojure/core.cache "0.6.4"]
                 [org.clojure/core.memoize "0.5.6"]])
