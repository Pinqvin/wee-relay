(ns wee-relay.ios.ui
  (:require [reagent.core :as r]))

(def tab-bar (r/adapt-react-class (.-TabBarIOS js/React)))
(def tab-bar-item (r/adapt-react-class (.-TabBarIOS.Item js/React)))
