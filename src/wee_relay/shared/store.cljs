(ns wee-relay.shared.store
  (:require [reagent.core :as r]))

(set! js/React (js/require "react-native"))

(def async-storage (.-AsyncStorage js/React))

(def +settings-key+ "@WeeRelaySettings")

(defn load [item-key result-fn]
  (-> (.getItem async-storage (str +settings-key+ item-key))
      (.then result-fn)))

(defn save [item-key value]
  (.setItem async-storage (str +settings-key+ item-key) value))
