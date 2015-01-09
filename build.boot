(set-env!
  :source-paths #{"src"}
  :resource-paths #{"resources"}
  :dependencies '[[adzerk/boot-cljs "0.0-2629-3" :scope "test"]
                  [adzerk/boot-cljs-repl "0.1.7" :scope "test"]
                  [adzerk/boot-reload "0.2.3" :scope "test"]
                  [pandeiro/boot-http "0.4.2" :scope "test"]
                  [cljsjs/boot-cljsjs "0.4.0" :scope "test"]
                  [org.clojure/core.async "0.1.346.0-17112a-alpha" :scope "test"]
                  [rum "0.1.0" :scope "test"]
                  [com.cognitect/transit-cljs "0.8.199" :scope "test"]])

(require
  '[adzerk.boot-cljs :refer [cljs]]
  '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
  '[adzerk.boot-reload :refer [reload]]
  '[pandeiro.boot-http :refer [serve]]
  '[cljsjs.boot-cljsjs :refer [from-jars]])

(deftask debug
  "Build for debugging & development"
  []
  (comp (serve :dir "target" :port 8000)
        (from-jars :path "react/react.min.js" :target "js/react.inc.js")
        (watch)
        (speak)
        (cljs-repl)
        (cljs :optimizations :none :source-map true :unified-mode true)
        (reload)))

(deftask release
  "Package for release"
  []
  (comp (speak)
        (from-jars :package true :path "react/react.min.js" :target "js/react.inc.js")
        (cljs :optimizations :advanced)))
