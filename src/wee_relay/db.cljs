(ns wee-relay.db
  (:require [schema.core :as s :include-macros true]))

;; schema of app-db
(def schema {:ios {:tab s/Str}
             :settings {(s/optional-key :host) s/Any
                        (s/optional-key :port) s/Any
                        (s/optional-key :password) s/Any}
             :server {:connection s/Any
                      :connecting? s/Bool
                      :connection-failed? s/Bool
                      :authentication-failed? s/Bool}})

;; initial state of app-db
(def app-db {:ios {:tab "list"}
             :settings {}
             :server {:connection nil
                      :connecting? false
                      :connection-failed? false
                      :authentication-failed? false}})
