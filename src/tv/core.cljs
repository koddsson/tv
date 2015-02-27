(ns tv.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [clojure.string :refer [blank? lower-case]]
            [cljs.core.async :refer [<! timeout]]
            [rum :refer-macros [defc]]
            [tv.utils :refer [get-json! format-date get-weekday get-date-classes]]))

(def state (atom {}))

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
    [:span {:style {:color "hotpink"}} "sjónvarpsdagsskrá "
     [:span {:style {:color "lime" :font-style "italic"}} weekday]]]])

(defc tv-schedule < rum/static [schedule]
  [:section#tv-schedule.container.animated.fadeIn
   (for [{:keys [description originalTitle startTime title]} schedule]
     [:div.tv-show {:class (get-date-classes startTime)}
      [:h2 title
       (if-not (or (blank? originalTitle)
                   (= (lower-case originalTitle) (lower-case title)))
         [:small {:style {:color "teal"}}
          (str " (" originalTitle ")")])
       [:p [:small {:style {:color "darkslateblue"}}
            (format-date startTime "HH:mm")]]]
      [:p description]])])

(defc app < rum/reactive []
  (let [{:keys [schedule]} (rum/react state)]
    (if (seq schedule)
      [:div.text-center
       (header (get-weekday (:startTime (first schedule))))
       (tv-schedule schedule)]
      [:img#loader {:src "img/hourglass.svg"}])))

(defn ^:export mount [element]
  (get-schedule!)
  (rum/mount (app) element))
