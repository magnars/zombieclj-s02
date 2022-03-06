(ns zombies.client.main
  (:require [chord.client :refer [ws-ch]]
            [cljs.core.async :refer [<! close! put!]]
            [dumdom.core :as dumdom]
            [zombies.client.actions :as actions]
            [zombies.client.components :refer [Page]]
            [zombies.client.event-bus :as bus])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defonce ws-atom (atom nil))
(defonce store (atom nil))
(defonce container (js/document.getElementById "main"))

(defn connect-to-ws []
  (go
    (let [{:keys [ws-channel error]} (<! (ws-ch "ws://localhost:8666/ws"))]
      (when error (throw error))
      (reset! ws-atom ws-channel)
      (reset! store (:message (<! ws-channel)))
      (loop []
        (when-let [actions (:message (<! ws-channel))]
          (actions/perform-actions store actions)
          (recur))))))

(bus/watch ::me :perform-actions #(actions/perform-actions store %))
(bus/watch ::me :send-command #(put! @ws-atom %))

(defn render [& _]
  (dumdom/render (Page @store) container))

(add-watch store ::me render)

(defonce only-once
  (connect-to-ws))

(defn reset []
  (close! @ws-atom)
  (connect-to-ws))

(comment

  (reset)
  (connect-to-ws)


  )
