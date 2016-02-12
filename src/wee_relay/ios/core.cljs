(ns wee-relay.ios.core
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [wee-relay.ios.scenes.main :refer [main-scene]]
            [wee-relay.shared.ui :as ui]
            [wee-relay.ios.routes :refer [routes]]
            [wee-relay.ios.styles :refer [styles]]
            [wee-relay.handlers]
            [wee-relay.subs]))

(def +route-mapper+ {:LeftButton (fn [route navigator index nav-state]
                                   (if (= index 2)
                                     (r/as-element
                                       [ui/touchable-opacity {:on-press (fn [] (.pop navigator))}
                                        [ui/text "Back"]])))
                     :RightButton (fn [route navigator index nav-state])
                     :Title (fn [route navigator index nav-state]
                              (r/as-element
                                [ui/text {:style (get-in styles [:navigation-bar :title])} (.-title route)]))})

(defn app-root []
  [ui/navigator {:initial-route (:main routes)
                 :navigation-bar (r/as-element [ui/navigation-bar {:route-mapper +route-mapper+
                                                                   :style (get-in styles [:navigation-bar :bar])}])
                 :render-scene (fn [route navigator]
                                 (let [route (js->clj route :keywordize-keys true)]
                                   (r/as-element
                                     (case (:name route)
                                       "main" [main-scene {:navigator navigator}]
                                       "channel" nil))))}])

(defn init []
  (dispatch-sync [:initialize-db])
  (dispatch [:load-from-db :host])
  (dispatch [:load-from-db :port])
  (dispatch [:load-from-db :password])
  (.registerComponent ui/app-registry "WeeRelay" #(r/reactify-component app-root)))
