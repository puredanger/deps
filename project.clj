(defproject puredanger/deps "0.0.1"
  :description "deps"
  :url "https://github.com/puredanger/deps"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main deps.main
  :aot :all
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [com.cemerick/pomegranate "0.3.1"]
                 [org.clojure/tools.cli "0.3.5"]]
  :repositories [["clojars" {:url "https://clojars.org/repo/"
                             :sign-releases false}]])
