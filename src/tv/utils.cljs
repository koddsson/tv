(ns tv.utils
  (:require [goog.net.XhrIo :as xhr]
            [clojure.walk :refer [keywordize-keys]]
            [cljs.core.async :refer [chan put!]]
            [cognitect.transit :as transit]))

(defn get-json!
  "Fetch and parse JSON data from the given URL"
  [url]
  (let [channel (chan)
        get-response-text #(.getResponseText (.-target %))
        parse #(keywordize-keys (transit/read (transit/reader :json) %))]
    (xhr/send url #(put! channel (parse (get-response-text %))))
    channel))

(defn day [date])

(defn display-date [date & [pattern]]
  (. (js/moment date) format (or pattern "HH:mm")))
