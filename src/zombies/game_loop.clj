(ns zombies.game-loop
  (:require [clojure.core.async :refer [<! go put!]]
            [clojure.core.match :refer [match]]
            [zombies.actionizer :as actionizer]
            [zombies.game :as game]))

(defn actionize [events]
  (try
    (mapcat actionizer/event->actions events)
    (catch Exception e
      [[:assoc-in [:server-error] (.getMessage e)]])))

(defn start [ws-channel]
  (put! ws-channel {})
  (put! ws-channel (actionize (game/kickstart-game (System/currentTimeMillis))))
  (go
    (loop []
      (when-let [command (:message (<! ws-channel))]
        (match command
          [:reroll] (put! ws-channel (actionize (game/reroll nil))))))))
