(ns zombies.game-loop
  (:require [clojure.core.async :refer [put!]]))

(defn start [ws-channel]
  (put! ws-channel {})
  (put! ws-channel
        [[:assoc-in [:zombies :zombie-1] {:id :zombie-1
                                          :kind 1
                                          :max-health 5}]
         [:wait 1000]
         [:show-tips {:position :at-zombies
                      :header "Zombiene kommer!"
                      :prose "Det er zombier overalt. En av dem har oppdaget deg. Du har også sett tre andre i nærheten."}]
         [:assoc-in [:player] {:max-health 9}]
         [:wait 1000]
         [:show-tips {:position :at-player-health
                      :header "Dine helsepoeng"
                      :prose "Disse små hjertene er alt som står mellom deg og de vandøde."}]]))
