(ns wee-relay.shared.scenes.settings
  (:require [re-frame.core :refer [dispatch subscribe]]
            [wee-relay.shared.ui :as ui]
            [wee-relay.ios.styles :refer [styles]]))

(defn settings-scene []
  (let [settings (subscribe [:get-settings])]
    (fn []
      [ui/view {:style (get-in styles [:settings :container])}
       [ui/text "Connection settings"]
       [ui/view {:style (get-in styles [:settings :row])}
        [ui/text "Hostname"]
        [ui/text-input {:style (get-in styles [:settings :input])
                        :auto-capitalize "none"
                        :value (:host @settings)
                        :on-change-text #(dispatch [:save-setting :host %1])
                        :placeholder "Test"}]]
       [ui/view {:style (get-in styles [:settings :row])}
        [ui/text "Port"]
        [ui/text-input {:style (get-in styles [:settings :input])
                        :auto-capitalize "none"
                        :keyboard-type "numeric"
                        :value (:port @settings)
                        :on-change-text #(dispatch [:save-setting :port %1])}]]
       [ui/view {:style (get-in styles [:settings :row])}
        [ui/text "Password"]
        [ui/text-input {:style (get-in styles [:settings :input])
                        :auto-capitalize "none"
                        :secure-text-entry true
                        :value (:password @settings)
                        :on-change-text #(dispatch [:save-setting :password %1])}]]])))
