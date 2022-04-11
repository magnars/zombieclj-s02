(ns zombies.game
  (:require [clojure.core.match :refer [match]]))

(def zombie
  {:id :zombie-1
   :kind 1
   :max-health 5})

(def faces [:punch :heal :shields :punches :shovel :skull])

(defn kickstart-game [seed]
  (let [rng (java.util.Random. seed)]
    [[:add-zombie {:id :zombie-1 :kind 1 :max-health 5}]
     [:show-tip {:id :zombies-intro
                 :position :at-zombies
                 :header "Zombiene kommer!"
                 :prose "Det er zombier overalt. En av dem har oppdaget deg. Du har også sett tre andre i nærheten."}]
     [:set-player-health 9]
     [:show-tip {:id :player-health-intro
                 :position :at-player-health
                 :header "Dine helsepoeng"
                 :prose "Disse små hjertene er alt som står mellom deg og de vandøde."}]
     [:add-dice (for [i (range 5)]
                  {:id (keyword (str "die-" i))
                   :current-face (mod (.nextInt rng) (count faces))
                   :faces faces})]
     [:set-player-rerolls 2]
     [:set-seed (inc seed)]]))

(defn reroll [{:keys [player dice seed]}]
  (let [target-reroll (inc (:used-rerolls player 0))
        rng (java.util.Random. seed)]
    (when (<= target-reroll (:rerolls player 0))
      (concat
       [[:use-reroll target-reroll]]
       (for [die (remove :locked? (vals dice))]
         [:reroll-die {:id (:id die)
                       :from (:current-face die)
                       :to (mod (.nextInt rng) (count (:faces die)))
                       :roll-id seed}])
       [[:set-seed (inc seed)]]))))

(defn toggle-clamp [game die-id]
  [[:set-die-lock die-id (not (get-in game [:dice die-id :locked?]))]])

(defn face? [die face]
  (= face (nth (:faces die) (:current-face die))))

(defn use-dice [game {:keys [target]}]
  (let [punches (apply + (map #(cond
                                 (face? % :punch) 1
                                 (face? % :punches) 2
                                 :else 0)
                              (vals (:dice game))))]
    (if (< 0 punches)
      [[:punch-zombie target punches]]
      [])))

(defn update-game [game event]
  (match event
    [:add-dice dice] (update game :dice merge (into {} (map (juxt :id identity) dice)))
    [:add-zombie zombie] (assoc-in game [:zombies (:id zombie)] zombie)
    [:reroll-die m] (assoc-in game [:dice (:id m) :current-face] (:to m))
    [:set-die-lock die-id b] (assoc-in game [:dice die-id :locked?] b)
    [:set-player-health n] (assoc-in game [:player :max-health] n)
    [:set-player-rerolls n] (assoc-in game [:player :rerolls] n)
    [:set-seed n] (assoc game :seed n)
    [:show-tip tip] game
    [:use-reroll n] (assoc-in game [:player :used-rerolls] n)))
