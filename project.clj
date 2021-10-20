(defproject crache "1.0.0-rc1"
  :description "Redis-backed caching and memoization, following core.cache and core.memoize"
  :license {:name "Eclipse Public License" :url "http://www.eclipse.org/legal/epl-v10.html"}
  :url "http://github.com/strongh/crache"
  :plugins [[lein-tools-deps "0.4.5"]]
  :middleware [lein-tools-deps.plugin/resolve-dependencies-with-deps-edn]
  :lein-tools-deps/config {:config-files [:install :user :project]}

  :mirrors {"clojure" {:url "https://build.clojure.org/releases/"}
            "clojure-snapshots" {:url "https://build.clojure.org/snapshots/"}
            "sonatype" {:url "https://oss.sonatype.org/content/repositories/releases"}
            "sonatype-snapshots" {:url "https://oss.sonatype.org/content/repositories/snapshots"}}

  )
