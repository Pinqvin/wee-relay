(ns wee-relay.db
  (:require [schema.core :as s :include-macros true]))

;; schema of app-db
(def schema {:ios {:tab s/Str}
             :settings {(s/optional-key :host) s/Str
                        (s/optional-key :port) s/Str
                        (s/optional-key :password) s/Str}
             :connection s/Any})

;; initial state of app-db
(def app-db {:ios {:tab "list"}
             :settings {}
             :connection nil})
