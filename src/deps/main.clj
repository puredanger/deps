(ns deps.main
  (:require
    [clojure.java.shell :as shell]
    [clojure.string :as str]
    [clojure.tools.cli :as cli]
    [deps.core :as dep])
  (:gen-class)
  (:import (java.lang ProcessBuilder$Redirect)))

(defn- transform-dep
  [s]
  (let [[g a v :as parts] (str/split s #"/")]
    [(symbol g a) (or v "RELEASE")]))

(defn- dep-parser
  [deps]
  (mapv transform-dep (str/split deps #",")))

(defn- start-jvm
  [classpath main]
  (let [pb (doto (ProcessBuilder. ["java" "-cp" classpath main])
             (.redirectInput ProcessBuilder$Redirect/INHERIT)
             (.redirectOutput ProcessBuilder$Redirect/INHERIT)
             (.redirectError ProcessBuilder$Redirect/INHERIT))
        p (.start pb)]
    (.waitFor p)))


(def cli-opts
  [["-d" "--deps DEP,..." "Dependencies"
    :default "org.clojure/clojure"]
   ["-cp" "--classpath" "Compute and print classpath"]
   ["-m" "--main NS" "Main namespace"
    :default "clojure.main"]
   ["-h" "--help"]])

(defn -main [& args]
  (let [{:keys [options] :as argmap} (cli/parse-opts args cli-opts)
        {:keys [deps classpath main help]} options]
    (cond
      help (println (:summary argmap))
      classpath (dep/compute-classpath (dep-parser deps))
      main (start-jvm (dep/compute-classpath (dep-parser deps)) main)
      :else (println (:summary argmap)))))

(comment
  (dep-parser "org.clojure/clojure")
  (dep/compute-classpath (dep-parser "org.clojure/clojure"))
  (-main "--classpath")
  (-main)
  (clojure.repl/pst *e)
  )