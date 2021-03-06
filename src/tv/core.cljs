(ns tv.core
  (:require-macros
    [clojure.core.strint :refer [<<]]
    [cljs.core.async.macros :refer [go]])
  (:require
    [clojure.string :refer [blank? lower-case]]
    [cljs.core.async :refer [<!]]
    [rum]
    [tv.utils :refer [get-json!  format-date get-end-time has-finished?]]))

(def state (atom {:error nil :schedule [] :station nil}))

(defonce stations
  #{{:ruv    {:nom "RÚV"     :gen "RÚVs"}}
    {:stod2  {:nom "Stöð 2"  :gen "Stöðvar 2"}}
    {:stod3  {:nom "Stöð 3"  :gen "Stöðvar 3"}}
    {:skjar1 {:nom "Skjár 1" :gen "Skjás 1"}}})

(defn get-station [id]
  (some #(and (get % (keyword id)) %) stations))

(defn get-schedule! [& [station]]
  (go
    (let [station (or (keyword station) :ruv)
          url (str "http://apis.is/tv/" (name station))
          {:keys [results]} (<! (get-json! url))]
      (if (seq results)
        (let [schedule (remove #(has-finished? %) results)]
          (swap! state assoc :schedule schedule :station station))
        (swap! state assoc :error "Það kom upp smá vandamál...")))))

(rum/defc error-message < rum/static [error]
  [:section#errors
   [:p.bg-danger error]])

(rum/defc tv-show < rum/static
  [{:keys [description duration originalTitle startTime title] :as show}]
  (let [end-time (get-end-time show)]
    [:div.tv-show
     [:h2 title
      (if-not (or (blank? originalTitle)
                  (= (lower-case originalTitle) (lower-case title)))
        [:small {:style {:color "teal"}}
         (<< " (~{originalTitle})")])
      [:p [:small {:style {:color "darkslateblue"}}
           (<< "~(format-date startTime \"HH:mm\") til "
               "~(format-date end-time \"HH:mm\")")]]]
     [:p description]]))

(rum/defc tv-schedule < rum/static [station schedule]
  [:section#tv-schedule.animated.fadeIn
   (for [{:keys [startTime] :as show} schedule]
     (let [timestamp (format-date startTime "x")
           rum-key (<< "~(name station)-~{timestamp}")]
       (rum/with-props tv-show show :rum/key rum-key)))])

(rum/defc main < rum/reactive []
  (let [{:keys [error schedule station]} (rum/react state)]
    (conj [:div#rum-components]
          (error-message error)
          (tv-schedule station schedule))))

(defn ^:export mount [element]
  (get-schedule!)
  (rum/mount (main) element))
