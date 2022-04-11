(ns zombies.actionizer
  (:require [clojure.core.match :refer [match]]))

(def punch-names
  (cycle ["punched-1"
          "punched-4"
          "punched-2"
          "punched-5"
          "punched-3"]))

(defn punch-zombie [{:keys [target die-ids punches health]}]
  (concat
   (for [id die-ids]
     [:assoc-in [:dice id :status] :using])
   (mapcat
    (fn [i punch]
      (cond->
          [[:assoc-in [:zombies target :punches] [punch]]
           [:assoc-in [:zombies target :health] (- health i 1)]
           [:wait 200]]
        (= 0 (- health i 1))
        (conj [:assoc-in [:zombies target :falling?] true])))
    (range)
    (take punches punch-names))
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
