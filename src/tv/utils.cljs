(ns tv.utils
  (:require [goog.net.XhrIo :as xhr]
            [clojure.walk :refer [keywordize-keys]]
            [cljs.core.async :refer [chan put!]]
            [cognitect.transit :as transit]))

(defn get-json!
  "Fetch and parse JSON data from the given URL"
  [url]
  (let [channel (chan)
        response-text #(.getResponseText (.-target %))
        parse #(keywordize-keys (transit/read (transit/reader :json) %))]
    (xhr/send url #(put! channel (parse (response-text %))))
    channel))

(defn format-date [date & [fmt]]
  (. (js/moment date) format (or fmt "LLLL")))

(defn get-weekday [date]
  (let [weekdays ["sunnu" "mánu" "þriðju" "miðviku"
                  "fimmtu" "föstu" "laugar"]]
    (nth weekdays (. (js/moment date) weekday))))
