(ns wee-relay.ios.scenes.main
  (:require [reagent.core :as r]
            [re-frame.core :refer [subscribe dispatch]]
            [wee-relay.ios.ui :as ios-ui]
            [wee-relay.ios.styles :refer [styles]]
            [wee-relay.ios.scenes.list :refer [list-scene]]
            [wee-relay.shared.ui :as ui]
            [wee-relay.shared.scenes.settings :refer [settings-scene]]))

(defn main-scene [{navigator :navigator}]
  (let [active-tab (subscribe [:get-ios-tab])]
    [ios-ui/tab-bar
     [ios-ui/tab-bar-item {:on-press #(dispatch [:set-ios-tab "list"])
                           :selected (= @active-tab "list")
                           :title "Channels"}
      [list-scene]]
     [ios-ui/tab-bar-item {:on-press #(dispatch [:set-ios-tab "settings"])
                           :selected (= @active-tab "settings")
                           :title "Settings"}
      [settings-scene]]]))
