(ns wee-relay.handlers
  (:require
    [re-frame.core :refer [register-handler after trim-v dispatch]]
    [schema.core :as s :include-macros true]
    [wee-relay.shared.store :as store]
    [wee-relay.socket :as socket]
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

(register-handler
  :connect-to-server
  [validate-schema-mw trim-v]
  (fn [{:keys [settings] :as db}]
    (let [url (str "ws://" (:host settings) ":" (:port settings) "/weechat")]
      (if-let [connection (js/WebSocket. url)]
        (dispatch [:connection-successful connection])
        (dispatch [:connection-failed]))
      (assoc-in db [:server :connecting?] true))))

(register-handler
  :connection-successful
  [validate-schema-mw trim-v]
  (fn [db [connection]]
    (set! (.-onmessage connection) socket/on-message)
    (set! (.-onopen connection) socket/on-open)
    (set! (.-onclose connection) socket/on-close)
    (set! (.-onerror connection) socket/on-error)
    (assoc-in db [:server :connection] connection)))

(register-handler
  :connection-failed
  [validate-schema-mw trim-v]
  (fn [db]
    (-> (assoc-in db [:server :connecting?] false)
        (assoc-in [:server :connection-failed?] true))))

(register-handler
  :authenticate
  [validate-schema-mw trim-v]
  (fn [{:keys [server settings] :as db}]
    (.send (:connection server) (str "init password=" (:password settings) "\nhdata buffer:gui_buffers(*) number,full_name\n"))
    db))
