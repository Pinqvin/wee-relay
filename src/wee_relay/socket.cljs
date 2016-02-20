(ns wee-relay.socket
  (:require [re-frame.core :refer [dispatch]]
            [wee-relay.weechat :refer [decode-data]]))

(defn on-message [evt]
  (prn {:handler "on-message"
        :event (decode-data (.-data evt))}))

(defn on-open [evt]
  (dispatch [:authenticate]))

(defn on-close [evt]
  (prn {:handler "on-close"
        :event (.-data evt)}))

(defn on-error [evt]
  (prn {:handler "on-error"
        :event (.-data evt)}))
