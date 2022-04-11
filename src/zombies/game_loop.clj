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

(defn update-game [game events]
  (reduce game/update-game game events))

(defn start [ws-channel]
  (let [initial-game {}
        initial-events (game/kickstart-game (System/currentTimeMillis))]
    (put! ws-channel initial-game)
    (put! ws-channel (actionize initial-events))
    (go
      (loop [game (update-game initial-game initial-events)]
        (when-let [command (:message (<! ws-channel))]
          (let [events (match command
                         [:reroll] (game/reroll game)
                         [:toggle-clamp id] (game/toggle-clamp game id)
                         [:use-dice opts] (game/use-dice game opts))]
            (put! ws-channel (actionize events))
            (recur (update-game game events))))))))
