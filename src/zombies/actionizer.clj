(ns zombies.actionizer
  (:require [clojure.core.match :refer [match]]))

(defn event->actions [event]
  (match event
    [:add-zombie zombie] [[:assoc-in [:zombies (:id zombie)] zombie]]
    [:show-tips tips] [[:wait 1000] event]
    [:set-player-health n] [[:assoc-in [:player] {:max-health n}]]
    [:add-die die] [[:assoc-in [:dice (:id die)] (assoc die :entering? true)]]))
