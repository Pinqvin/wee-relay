(defproject wee-relay "0.1.0-SNAPSHOT"
  :description "WeeChat relay client for iOS"
  :license {:name "The MIT License (MIT)"
            :url  "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.170"]
                 [reagent "0.5.1" :exclusions [cljsjs/react]]
                 [re-frame "0.6.0"]
                 [prismatic/schema "1.0.4"]
                 [cljsjs/pako "0.2.7-0"]]
  :plugins [[lein-cljsbuild "1.1.1"]
            [lein-figwheel "0.5.0-2"]]
  :clean-targets ["target/" "index.ios.js" "index.android.js"]
  :aliases {"prod-build" ^{:doc "Recompile code with prod profile."}
            ["do" "clean"
             ["with-profile" "prod" "cljsbuild" "once" "ios"]
             ["with-profile" "prod" "cljsbuild" "once" "android"]]}
  :profiles {:dev {:dependencies [[figwheel-sidecar "0.5.0-2"]
                                  [com.cemerick/piggieback "0.2.1"]]
                   :source-paths ["src" "env/dev"]
                   :cljsbuild {:builds {:ios {:source-paths ["src" "env/dev"]
                                              :figwheel true
                                              :compiler {:output-to "target/ios/not-used.js"
                                                         :main "env.ios.main"
                                                         :output-dir "target/ios"
                                                         :optimizations :none}}
                                        :android {:source-paths ["src" "env/dev"]
                                                  :figwheel true
                                                  :compiler {:output-to "target/android/not-used.js"
                                                             :main "env.android.main"
                                                             :output-dir "target/android"
                                                             :optimizations :none}}}}
                   :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}}
             :prod {:cljsbuild {:builds {:ios {:source-paths ["src" "env/prod"]
                                               :compiler {:output-to "index.ios.js"
                                                          :main "env.ios.main"
                                                          :output-dir "target/ios"
                                                          :optimizations :simple}}
                                         :android {:source-paths ["src" "env/prod"]
                                                   :compiler {:output-to "index.android.js"
                                                              :main "env.android.main"
                                                              :output-dir "target/android"
                                                              :optimizations :simple}}}}}})
