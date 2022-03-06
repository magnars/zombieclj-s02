(ns zombies.game-loop
  (:require [clojure.core.async :refer [put!]]))

(defn start [ws-channel]
  (put! ws-channel {})
  (put! ws-channel [[:assoc-in [:zombies :zombie-1] {:id :zombie-1
                                                     :kind 1
                                                     :max-health 5}]]))
