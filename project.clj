(defproject tv "0.2.0-SNAPSHOT"
  :author "Paul Burt"
  :description "What's on Icelandic TV today?"
  :min-lein-version "2.5.0"
  :dependencies [[org.clojure/clojure "1.6.0"]]
  :profiles
  {:dev
   {:dependencies [[org.clojure/clojurescript "0.0-2913"]
                   [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                   [org.clojure/core.incubator "0.1.3"]
                   [rum "0.2.5"]
                   [sablono "0.3.4"]
                   [com.cognitect/transit-cljs "0.8.205"]
                   [cljsjs/moment "2.9.0-0"]]
    :plugins [[lein-cljsbuild "1.0.4"]]
    :hooks [leiningen.cljsbuild]
    :clean-targets ^{:protect false} ["assets/js" "target"]
    :cljsbuild
    {:builds
     [{:id "debug"
       :source-paths ["src"]
       :compiler {:main tv.core
                  :optimizations :none
                  :output-dir "assets/js/out"
                  :output-to "assets/js/main.js"
                  :source-map true}}
      {:id "release"
       :source-paths ["src"]
       :compiler {:elide-asserts true
                  :optimizations :advanced
                  :output-to "assets/js/main.js"
                  :pretty-print false}}]}}})
