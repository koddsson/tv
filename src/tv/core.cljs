(ns tv.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [clojure.string :refer [blank? lower-case]]
            [cljs.core.async :refer [<! timeout]]
            [rum :refer-macros [defc]]
            [tv.utils :refer [get-json! format-date get-weekday]]))

(def state (atom {:schedule []}))

(defn get-schedule! []
  (go
    (<! (timeout 2000))
    (let [url "http://apis.is/tv/ruv"
          {:keys [results]} (<! (get-json! url))]
      (if (seq results)
        (swap! state assoc :schedule results)))))

(defc header < rum/static [weekday]
  [:header.jumbotron
   [:h1.animated.rubberBand
    [:span.fuchsia "sjónvarps"
     [:span.yellow "DOGEskrá "
      [:span.aqua weekday
       [:span.lime " (vá!)"]]]]]])

(defc tv-schedule < rum/static [schedule]
  [:section#tv-schedule.container.animated.fadeIn
   (for [{:keys [description originalTitle startTime title]} schedule]
     [:div.tv-show
      [:h2 title
       (if-not (or (blank? originalTitle)
                   (= (lower-case originalTitle) (lower-case title)))
         [:small.fuchsia (str " (" originalTitle ")")])
       [:p [:small.blue (format-date startTime "HH:mm")]]]
      [:p description]])])

(defc app < rum/reactive []
  (let [{:keys [schedule]} (rum/react state)]
    (if (seq schedule)
      [:div.text-center
       (header (get-weekday (:startTime (first schedule))))
       (tv-schedule schedule)]
      [:img#loader {:src "img/loader.gif" :width 160 :height 24}])))

(defn ^:export mount [element]
  (get-schedule!)
  (rum/mount (app) element))
