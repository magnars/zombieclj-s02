(ns zombies.actionizer
  (:require [clojure.core.match :refer [match]]))

(defn event->actions [event]
  (match event
    [:add-zombie zombie] [[:assoc-in [:zombies (:id zombie)] zombie]]
    [:reroll-die m] [[:assoc-in [:dice (:id m) :current-face] (:to m)]
                     [:assoc-in [:dice (:id m) :previous-face] (:from m)]]
    [:show-tip tip] [event]
    [:set-player-health n] [[:assoc-in [:player :max-health] n]]
    [:set-player-rerolls n] [[:assoc-in [:player :rerolls] n]]
    [:use-reroll n] [[:assoc-in [:player :used-rerolls] n]]
    [:set-seed n] nil
    [:add-dice dice] (concat (for [die dice]
                               [:assoc-in [:dice (:id die)] (assoc die :entering? true)])
                             [[:wait (+ 1800 (* 100 (count dice)))]])))
