(ns tv.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [clojure.string :refer [blank? lower-case]]
            [cljs.core.async :refer [<!]]
            [rum :include-macros true]
            [tv.utils :refer [get-json! format-date get-weekday]]))

(def state (atom {:schedule []}))

(defn get-schedule! []
  (go
    (let [url "http://apis.is/tv/ruv"
          {:keys [results]} (<! (get-json! url))]
      (if (seq results)
        (swap! state assoc :schedule results)))))

(rum/defc rum/static header [weekday]
  [:header.jumbotron.text-center
   [:h1.animated.rubberBand
    [:span.fuchsia "sjónvarps"
     [:span.yellow "DOGEskrá "
      [:span.aqua weekday
       [:span.lime " (vá!)"]]]]]])

(rum/defc rum/static tv-show
  [{:keys [description originalTitle startTime title]}]
  [:div.tv-show
   [:h2 title
    (if-not (or (blank? originalTitle)
                (= (lower-case originalTitle) (lower-case title)))
      [:small.fuchsia (str " (" originalTitle ")")])
    [:p [:small.blue (format-date startTime "HH:mm")]]]
   [:p description]])

(rum/defc rum/static tv-schedule [schedule]
  [:section#tv-schedule.container.animated.fadeIn.text-center
   (map tv-show schedule)])

(rum/defc rum/reactive app []
  (let [{:keys [schedule]} (rum/react state)]
    (if (seq schedule)
      [:div
       (header (get-weekday (:startTime (first schedule))))
       (tv-schedule schedule)])))

(get-schedule!)

(rum/mount (app) (.-body js/document))
