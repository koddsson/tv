(ns tv.core
  (:require-macros
    [clojure.core.strint :refer [<<]]
    [cljs.core.async.macros :refer [go]])
  (:require
    [clojure.string :refer [blank? lower-case]]
    [cljs.core.async :refer [<!]]
    [rum]
    [tv.utils :refer [get-json! format-date has-finished?]]))

(def state (atom {:ready? false :schedule [] :station nil}))

(defonce stations
  #{{:ruv    {:nom "RÚV"     :gen "RÚVs"}}
    {:stod2  {:nom "Stöð 2"  :gen "Stöðvar 2"}}
    {:stod3  {:nom "Stöð 3"  :gen "Stöðvar 3"}}
    {:skjar1 {:nom "Skjár 1" :gen "Skjás 1"}}})

(defn get-station [id]
  (some #(and (get % id) %) stations))

(defn get-schedule! [& [station]]
  (go
    (let [station (or (keyword station) :ruv)
          url (str "http://apis.is/tv/" (name station))
          {:keys [results]} (<! (get-json! url))]
      (if (seq results)
        (let [schedule (remove
                         #(has-finished? (:startTime %) (:duration %))
                         results)]
          (swap! state assoc :ready?   true
                             :schedule schedule
                             :station  (get-station station)))))))

(rum/defc tv-show < rum/static
  [{:keys [description duration originalTitle startTime title]}]
  [:div.tv-show
   [:h2 title
    (if-not (or (blank? originalTitle)
                (= (lower-case originalTitle) (lower-case title)))
      [:small {:style {:color "teal"}}
       (<< " (~{originalTitle})")])
    [:p [:small {:style {:color "darkslateblue"}}
         (format-date startTime "HH:mm")]]]
   [:p description]])

(rum/defc tv-schedule < rum/static [schedule station]
  [:section#tv-schedule.animated.fadeIn
   (map tv-show schedule)])

(rum/defc main < rum/reactive []
  (let [{:keys [ready? schedule station]} (rum/react state)]
    (conj [:div#rum-components]
          (if ready?
            (tv-schedule schedule station)
            [:img#loader {:src "img/hourglass.svg"}]))))

(defn ^:export mount [element]
  (get-schedule!)
  (rum/mount (main) element))
