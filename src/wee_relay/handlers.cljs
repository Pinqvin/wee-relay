(ns wee-relay.handlers
  (:require
    [re-frame.core :refer [register-handler after trim-v dispatch]]
    [schema.core :as s :include-macros true]
    [wee-relay.shared.store :as store]
    [wee-relay.db :as db :refer [app-db schema]]))

;; -- Middleware
;;

(defn check-and-throw
  "throw an exception if db doesn't match the schema."
  [a-schema db]
  (if-let [problems (s/check a-schema db)]
    (throw (js/Error. (str "schema check failed: " problems)))))

(def validate-schema-mw
  (after (partial check-and-throw schema)))

;; -- Handlers

(register-handler
  :initialize-db
  validate-schema-mw
  (fn [_ _]
    app-db))

(register-handler
  :load-from-db
  [validate-schema-mw trim-v]
  (fn [db [value]]
    (store/load value (fn [response]
                        (let [handler-name (-> (str "load-from-db-" (name value))
                                               (keyword))]
                          (dispatch [handler-name response]))))
    db))

(register-handler
  :load-from-db-host
  [validate-schema-mw trim-v]
  (fn [db [data]]
    (assoc-in db [:settings :host] data)))

(register-handler
  :load-from-db-port
  [validate-schema-mw trim-v]
  (fn [db [data]]
    (assoc-in db [:settings :port] data)))

(register-handler
  :load-from-db-password
  [validate-schema-mw trim-v]
  (fn [db [data]]
    (assoc-in db [:settings :password] data)))

(register-handler
  :save-setting
  [validate-schema-mw trim-v]
  (fn [db [key value]]
    (store/save key value)
    (assoc-in db [:settings key] value)))

(register-handler
  :set-ios-tab
  [validate-schema-mw trim-v]
  (fn [db [value]]
    (assoc-in db [:ios :tab] value)))
