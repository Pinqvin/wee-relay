(ns wee-relay.ios.styles)

(def +light-gray+ "#EAEAEA")

(def styles {:settings {:container {:flex-direction "column"
                                    :padding-top 70
                                    :background-color +light-gray+
                                    :flex 1
                                    :align-items "stretch"}
                        :row {:padding 40
                              :background-color "white"}
                        :input {:height 25
                                :border-color "gray"
                                :border-width 0.5}}
             :list {:container {:flex-direction "column"
                                :padding-top 70
                                :background-color +light-gray+
                                :flex 1
                                :align-items "center"}}
             :navigation-bar {:bar {:background-color "white"}
                              :title {:font-size 16
                                      :margin-vertical 10}}})
