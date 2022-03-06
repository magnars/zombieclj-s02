(ns zombies.game-loop
  (:require [clojure.core.async :refer [put!]]
            [zombies.actionizer :as actionizer]
            [zombies.game :as game]))

(defn start [ws-channel]
  (put! ws-channel {})
  (put! ws-channel
        (try
          (let [events (game/kickstart-game (System/currentTimeMillis))]
            (mapcat actionizer/event->actions events))
          (catch Exception e
            [[:assoc-in [:server-error] (.getMessage e)]]))))
