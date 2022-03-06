(ns ^:figwheel-hooks zombies.client.main
  (:require [chord.client :refer [ws-ch]]
            [cljs.core.async :refer [<!]]
            [dumdom.core :as dumdom]
            [zombies.client.components :refer [Page]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defonce ws-atom (atom nil))
(defonce container (js/document.getElementById "main"))

(defn connect-to-ws []
  (go
    (let [{:keys [ws-channel error]} (<! (ws-ch "ws://localhost:8666/ws"))]
      (when error (throw error))
      (reset! ws-atom ws-channel)
      (println (:message (<! ws-channel))))))

(defn ^:after-load render []
  (dumdom/render (Page nil) container))

(comment
  (connect-to-ws)


  )
