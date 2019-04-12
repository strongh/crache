(defproject crache "1.0.0-rc1-EMPEAR-3"
  :description "Redis-backed caching and memoization, following core.cache and core.memoize"
  :license {:name "Eclipse Public License" :url "http://www.eclipse.org/legal/epl-v10.html"}
  :url "http://github.com/strongh/crache"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [com.taoensso/carmine "2.7.0"]
                 [org.clojure/core.cache "0.6.4"]
                 [org.clojure/core.memoize "0.5.6"]]

  :mirrors {"clojure" {:url "https://build.clojure.org/releases/"}
            "clojure-snapshots" {:url "https://build.clojure.org/snapshots/"}
            "sonatype" {:url "https://oss.sonatype.org/content/repositories/releases"}
            "sonatype-snapshots" {:url "https://oss.sonatype.org/content/repositories/snapshots"}}

  )
