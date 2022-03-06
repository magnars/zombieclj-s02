(ns zombies.game)

(def zombie
  {:id :zombie-1
   :kind 1
   :max-health 5})

(def faces [:punch :heal :shields :punches :shovel :skull])

(defn kickstart-game [seed]
  (let [rng (java.util.Random. seed)]
    [[:add-zombie {:id :zombie-1 :kind 1 :max-health 5}]
     [:show-tips {:id :zombies-intro
                  :position :at-zombies
                  :header "Zombiene kommer!"
                  :prose "Det er zombier overalt. En av dem har oppdaget deg. Du har også sett tre andre i nærheten."}]
     [:set-player-health 9]
     [:show-tips {:id :player-health-intro
                  :position :at-player-health
                  :header "Dine helsepoeng"
                  :prose "Disse små hjertene er alt som står mellom deg og de vandøde."}]
     [:add-dice (for [i (range 5)]
                  {:id (keyword (str "die-" i))
                   :current-face (nth faces (mod (.nextInt rng) (count faces)))
                   :faces faces})]
     [:set-player-rerolls 2]]))
