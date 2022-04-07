(ns zombies.game-test
  (:require [clojure.test :refer [deftest is testing]]
            [zombies.game :as sut]))

(deftest kickstart-game
  (is (= (sut/kickstart-game 0)
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
          [:add-dice [{:id :die-0
                       :current-face :shields
                       :faces [:punch :heal :shields :punches :shovel :skull]}
                      {:id :die-1
                       :current-face :shovel
                       :faces [:punch :heal :shields :punches :shovel :skull]}
                      {:id :die-2
                       :current-face :shields
                       :faces [:punch :heal :shields :punches :shovel :skull]}
                      {:id :die-3
                       :current-face :punch
                       :faces [:punch :heal :shields :punches :shovel :skull]}
                      {:id :die-4
                       :current-face :punch
                       :faces [:punch :heal :shields :punches :shovel :skull]}]]
          [:set-player-rerolls 2]])))

(deftest update-game
  (is (= (sut/update-game {} [:add-zombie {:id :zombie-1 :kind 1 :max-health 5}])
         {:zombies {:zombie-1 {:id :zombie-1 :kind 1 :max-health 5}}}))

  (is (= (sut/update-game {} [:show-tip {:id :zombies-intro}])
         {}))

  (is (= (sut/update-game {} [:set-player-health 9])
         {:player {:max-health 9}}))

  (is (= (sut/update-game {} [:add-dice [{:id :die-0
                                          :current-face :shields
                                          :faces [:punch :heal :shields :punches :shovel :skull]}
                                         {:id :die-1
                                          :current-face :shovel
                                          :faces [:punch :heal :shields :punches :shovel :skull]}]])
         {:dice {:die-0 {:id :die-0
                         :current-face :shields
                         :faces [:punch :heal :shields :punches :shovel :skull]}
                 :die-1 {:id :die-1
                         :current-face :shovel
                         :faces [:punch :heal :shields :punches :shovel :skull]}}}))

  (is (= (-> {}
             (sut/update-game [:add-dice [{:id :die-0
                                           :current-face :shields
                                           :faces [:punch :heal :shields :punches :shovel :skull]}]])
             (sut/update-game [:add-dice [{:id :die-1
                                           :current-face :shovel
                                           :faces [:punch :heal :shields :punches :shovel :skull]}]]))
         {:dice {:die-0 {:id :die-0
                         :current-face :shields
                         :faces [:punch :heal :shields :punches :shovel :skull]}
                 :die-1 {:id :die-1
                         :current-face :shovel
                         :faces [:punch :heal :shields :punches :shovel :skull]}}}))

  (is (= (sut/update-game {} [:set-player-rerolls 2])
         {:player {:rerolls 2}}))

  (is (= (sut/update-game {:player {:rerolls 2}} [:use-reroll 1])
         {:player {:rerolls 2
                   :used-rerolls 1}})))

(deftest reroll
  (is (= (sut/reroll {:player {}})
         nil))

  (is (= (sut/reroll {:player {:rerolls 2}})
         [[:use-reroll 1]]))

  (is (= (sut/reroll {:player {:rerolls 2 :used-rerolls 1}})
         [[:use-reroll 2]]))

  (is (= (sut/reroll {:player {:rerolls 2 :used-rerolls 2}})
         nil)))
