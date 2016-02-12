(ns wee-relay.ios.scenes.list
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch subscribe]]
            [wee-relay.shared.ui :as ui]
            [wee-relay.ios.styles :refer [styles]]))

(defn channel-list [settings]
  (r/create-class
    {:component-will-mount #(dispatch [:connect-to-server])
     :reagent-render
     (fn [settings]
       [ui/text "Kanavalista"])}))

(defn list-scene []
  (let [settings (subscribe [:get-settings])]
    (fn []
      (let [{:keys [host port password]} @settings]
        [ui/view {:style (get-in styles [:list :container])}
         (if (and host port password)
           [channel-list @settings]
           [ui/text "Check settings"])]))))
