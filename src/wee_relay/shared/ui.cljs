(ns wee-relay.shared.ui
  (:require [reagent.core :as r]))

(set! js/React (js/require "react-native"))

(def app-registry (.-AppRegistry js/React))

(def navigator (r/adapt-react-class (.-Navigator js/React)))
(def navigation-bar (r/adapt-react-class (.-Navigator.NavigationBar js/React)))
(def touchable-opacity (r/adapt-react-class (.-TouchableOpacity js/React)))
(def view (r/adapt-react-class (.-View js/React)))
(def text (r/adapt-react-class (.-Text js/React)))
(def text-input (r/adapt-react-class (.-TextInput js/React)))
