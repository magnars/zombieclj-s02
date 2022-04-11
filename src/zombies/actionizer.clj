(ns zombies.actionizer
  (:require [clojure.core.match :refer [match]]))

(defn punch-zombie [{:keys [target die-ids punches health]}]
  (concat
   (for [id die-ids]
     [:assoc-in [:dice id :status] :using])
   [[:wait 1500]]
   (for [id die-ids]
     [:assoc-in [:dice id :status] :used])))

(defn event->actions [event]
  (match event
    [:add-dice dice] (concat (for [die dice]
                               [:assoc-in [:dice (:id die)] die])
                             [[:wait (+ 1800 (* 100 (count dice)))]])
    [:add-zombie zombie] [[:assoc-in [:zombies (:id zombie)] zombie]]
    [:punch-zombie opts] (punch-zombie opts)
    [:reroll-die m] [[:assoc-in [:dice (:id m) :roll-id] (:roll-id m)]
                     [:assoc-in [:dice (:id m) :current-face] (:to m)]
                     [:assoc-in [:dice (:id m) :previous-face] (:from m)]]
    [:set-die-lock die-id b] [[:assoc-in [:dice die-id :locked?] b]]
    [:set-player-health n] [[:assoc-in [:player :max-health] n]]
    [:set-player-rerolls n] [[:assoc-in [:player :rerolls] n]]
    [:set-seed n] nil
    [:show-tip tip] [event]
    [:use-reroll n] [[:assoc-in [:player :used-rerolls] n]]))
