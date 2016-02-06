(ns wee-relay.ios.scenes.list
  (:require [reagent.core :as r]
            [re-frame.core :refer [subscribe dispatch]]
            [wee-relay.ios.ui :as ios-ui]
            [wee-relay.ios.styles :refer [styles]]
            [wee-relay.shared.ui :as ui]
            [wee-relay.shared.scenes.settings :refer [settings-scene]]))

(defn list-scene [{navigator :navigator}]
  (let [active-tab (subscribe [:get-ios-tab])]
    [ios-ui/tab-bar
     [ios-ui/tab-bar-item {:on-press #(dispatch [:set-ios-tab "list"])
                           :selected (= @active-tab "list")
                           :title "Channels"}
      [ui/view {:style (get-in styles [:list :container])}
       [ui/text @active-tab]]]
     [ios-ui/tab-bar-item {:on-press #(dispatch [:set-ios-tab "settings"])
                           :selected (= @active-tab "settings")
                           :title "Settings"}
      [settings-scene]]]))
