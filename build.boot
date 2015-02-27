(set-env!
  :source-paths #{"src"}
  :resource-paths #{"resources"}
  :dependencies '[[adzerk/boot-cljs "0.0-2814-1" :scope "test"]
                  [adzerk/boot-cljs-repl "0.1.9" :scope "test"]
                  [adzerk/boot-reload "0.2.4" :scope "test"]
                  [pandeiro/boot-http "0.6.2" :scope "test"]
                  [cljsjs/boot-cljsjs "0.4.6" :scope "test"]
                  [org.clojure/core.async "0.1.346.0-17112a-alpha" :scope "test"]
                  [rum "0.2.5" :scope "test"]
                  [sablono "0.3.4" :scope "test"]
                  [com.cognitect/transit-cljs "0.8.205" :scope "test"]])

(require
  '[adzerk.boot-cljs :refer [cljs]]
  '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
  '[adzerk.boot-reload :refer [reload]]
  '[pandeiro.boot-http :refer [serve]])

(deftask debug
  "Build for debugging & development"
  []
  (comp (serve :dir "target" :port 8000)
        (watch)
        (speak)
        (cljs-repl)
        (cljs :optimizations :none :source-map true :unified-mode true)
        (reload)))

(deftask release
  "Package for release"
  []
  (comp (speak)
        (cljs :optimizations :advanced)))
