(ns tv.utils
  (:require [cljsjs.moment]
            [goog.net.XhrIo :as xhr]
            [clojure.walk :refer [keywordize-keys]]
            [cljs.core.async :refer [chan put!]]
            [cognitect.transit :as transit]))

(defn get-response-text [response]
  (.getResponseText (.-target response)))

(defn parse-json [json]
  (let [reader (transit/reader :json)]
    (keywordize-keys (transit/read reader json))))

(defn get-json!
  "Fetch and parse JSON data from the given URL"
  [url]
  (let [channel (chan)]
    (xhr/send url #(put! channel (parse-json (get-response-text %))))
    channel))

(defn format-date [date & [format]]
  (. (js/moment date) format (or format "LLLL")))

(defn get-weekday [date]
  (let [weekdays ["sunnu" "mánu" "þriðju" "miðviku"
                  "fimmtu" "föstu" "laugar"]]
    (nth weekdays (. (js/moment date) weekday))))

(defn get-end-time [{:keys [duration startTime]}]
  (let [duration (. js/moment (duration duration))]
    (. (js/moment startTime) (add duration))))

(defn has-finished? [show]
  (let [end-time (get-end-time show)]
    (not (. end-time isAfter))))
