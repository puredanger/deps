(ns deps.core
  (:require
    [clojure.string :as str]
    [cemerick.pomegranate.aether :as aether])
  (:import
    java.io.File))

(def standard-repos
  {"central" "https://repo1.maven.org/maven2/"
   "clojars" "https://clojars.org/repo/"})

(defn resolve-deps
  [coords]
  (aether/resolve-dependencies
    :repositories standard-repos
    :coordinates coords))

(defn compute-classpath
  [coords]
  (->> (resolve-deps coords)
    (aether/dependency-files)
    (filter #(re-find #"\.(jar|zip)$" (.getName %)))
    (map #(.getAbsolutePath %))
    (str/join File/pathSeparator)))

(comment
  (clojure.pprint/pprint (resolve-deps [['org.clojure/tools.analyzer.jvm "0.6.9"]]))
  (clojure.pprint/pprint (compute-classpath [['org.clojure/core.memoize "0.5.8"]]))

  (clojure.pprint/pprint (compute-classpath [['compojure/compojure "1.5.1"]]))
  (clojure.pprint/pprint (resolve-deps [['org.clojure/clojure "RELEASE"]]))
  (clojure.pprint/pprint (compute-classpath [['org.clojure/clojure "RELEASE"]]))

  )