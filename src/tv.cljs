(ns tv
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [clojure.string :refer [blank? lower-case]]
            [cljs.core.async :refer [<!]]
            [rum :include-macros true]
            [tv.utils :refer [day display-date get-json!]]))

(def state (atom {}))

(defn get-schedule! []
  (go
    (let [url "http://apis.is/tv/ruv"
          {:keys [results]} (<! (get-json! url))]
      (if (seq results)
        (swap! state assoc :schedule results)))))

(rum/defc rum/static header []
  [:header.jumbotron
   [:h1#title.animated.rubberBand
    [:span.magenta"mjög sjónvarp!  "
     [:span.cyan "svo dagsskrá!  "
      [:span.yellow "vá!"]]]]])

(rum/defc rum/static tv-show
  [{:keys [description originalTitle startTime title]}]
  [:div.tv-show
   [:h2 title
    (if-not (or (blank? originalTitle)
                (= (lower-case originalTitle)
                   (lower-case title)))
      [:small.magenta (str " (" originalTitle ")")])
    [:p [:small.blue (display-date startTime "HH:mm")]]]
   [:p description]])

(rum/defc rum/reactive tv-schedule []
  (get-schedule!)
  (let [{:keys [schedule]} (rum/react state)]
    (if (seq schedule)
      [:section#tv-shows.container.animated.fadeIn
       (map tv-show schedule)])))

(rum/defc app []
  [:div.text-center
   (header)
   (tv-schedule)])

(defn ^:export render [element]
  (rum/mount (app) element))
