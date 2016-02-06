(ns wee-relay.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub]]))

(register-sub
  :get-ios-tab
  (fn [db _]
    (reaction
      (get-in @db [:ios :tab]))))

(register-sub
  :get-settings
  (fn [db -]
    (reaction
      (get @db :settings))))
