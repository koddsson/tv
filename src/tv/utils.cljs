(ns tv.utils
  (:require [goog.net.XhrIo :as xhr]
            [clojure.walk :refer [keywordize-keys]]
            [cljs.core.async :refer [chan put!]]
            [cognitect.transit :as transit]))

(defn- parse-json [json]
  (let [reader (transit/reader :json)]
    (keywordize-keys (transit/read reader json))))

(defn get-json!
  "Fetch and parse JSON data from the given URL"
  [url]
  (let [channel (chan)
        response-text #(.getResponseText (.-target %))]
    (xhr/send url #(put! channel (parse-json (response-text %))))
    channel))

(defn has-begun? [date]
  (< (js/moment date) (js/moment)))

(defn format-date [date & [format]]
  (. (js/moment date) format (or format "LLLL")))

(defn get-weekday [date]
  (let [weekdays ["sunnu" "mánu" "þriðju" "miðviku"
                  "fimmtu" "föstu" "laugar"]]
    (nth weekdays (. (js/moment date) weekday))))
