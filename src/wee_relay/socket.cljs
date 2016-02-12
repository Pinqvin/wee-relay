(ns wee-relay.socket
  (:require [re-frame.core :refer [dispatch]]))

(defn on-message [evt]
  (prn {:handler "on-message"
        :event evt}))

(defn on-open [evt]
  (dispatch [:authenticate]))

(defn on-close [evt]
  (prn {:handler "on-close"
        :event evt}))

(defn on-error [evt]
  (prn {:handler "on-error"
        :event evt}))
