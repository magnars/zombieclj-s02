(ns zombies.game-test
  (:require [clojure.test :refer [deftest is testing]]
            [zombies.game :as sut]))

(deftest kickstart-game
  (is (= (sut/kickstart-game 0)
         [[:add-zombie {:id :zombie-1 :kind 1 :max-health 5}]
          [:show-tips {:position :at-zombies
                       :header "Zombiene kommer!"
                       :prose "Det er zombier overalt. En av dem har oppdaget deg. Du har også sett tre andre i nærheten."}]
          [:set-player-health 9]
          [:show-tips {:position :at-player-health
                       :header "Dine helsepoeng"
                       :prose "Disse små hjertene er alt som står mellom deg og de vandøde."}]
          [:add-die {:id :die-0
                     :current-face :shields
                     :faces [:punch :heal :shields :punches :shovel :skull]}]
          [:add-die {:id :die-1
                     :current-face :shovel
                     :faces [:punch :heal :shields :punches :shovel :skull]}]
          [:add-die {:id :die-2
                     :current-face :shields
                     :faces [:punch :heal :shields :punches :shovel :skull]}]
          [:add-die {:id :die-3
                     :current-face :punch
                     :faces [:punch :heal :shields :punches :shovel :skull]}]
          [:add-die {:id :die-4
                     :current-face :punch
                     :faces [:punch :heal :shields :punches :shovel :skull]}]])))
