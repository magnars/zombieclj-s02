(ns zombies.game-test
  (:require [clojure.test :refer [deftest is testing]]
            [zombies.game :as sut]))

(deftest kickstart-game
  (is (= (sut/kickstart-game 0)
         [[:add-zombie {:id :zombie-1 :kind 1 :max-health 5 :health 3}]
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
                       :current-face 2
                       :faces [:punch :heal :shields :punches :shovel :skull]}
                      {:id :die-1
                       :current-face 4
                       :faces [:punch :heal :shields :punches :shovel :skull]}
                      {:id :die-2
                       :current-face 2
                       :faces [:punch :heal :shields :punches :shovel :skull]}
                      {:id :die-3
                       :current-face 0
                       :faces [:punch :heal :shields :punches :shovel :skull]}
                      {:id :die-4
                       :current-face 0
                       :faces [:punch :heal :shields :punches :shovel :skull]}]]
          [:set-player-rerolls 2]
          [:set-seed 1]])))

(deftest add-zombie
  (is (= (sut/update-game {} [:add-zombie {:id :zombie-1 :kind 1 :max-health 5}])
         {:zombies {:zombie-1 {:id :zombie-1 :kind 1 :max-health 5}}})))

(deftest show-tip
  (is (= (sut/update-game {} [:show-tip {:id :zombies-intro}])
         {})))

(deftest set-player-health
  (is (= (sut/update-game {} [:set-player-health 9])
         {:player {:max-health 9}})))

(deftest add-dice
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
                         :faces [:punch :heal :shields :punches :shovel :skull]}}})))

(deftest add-multiple-dice
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
                         :faces [:punch :heal :shields :punches :shovel :skull]}}})))

(deftest set-player-rerolls
  (is (= (sut/update-game {} [:set-player-rerolls 2])
         {:player {:rerolls 2}})))

(deftest use-reroll
  (is (= (sut/update-game {:player {:rerolls 2}} [:use-reroll 1])
         {:player {:rerolls 2
                   :used-rerolls 1}})))

(deftest set-seed
  (is (= (sut/update-game {} [:set-seed 1])
         {:seed 1})))

(deftest reroll-die
  (is (= (sut/update-game {:dice {:die-0 {:id :die-0
                                          :current-face 1
                                          :faces [:punch :heal :shields :punches :shovel :skull]}
                                  :die-1 {:id :die-1
                                          :current-face 3
                                          :faces [:punch :heal :shields :punches :shovel :skull]}}}
                          [:reroll-die {:id :die-0 :from 0 :to 2}])
         {:dice {:die-0 {:id :die-0
                         :current-face 2
                         :faces [:punch :heal :shields :punches :shovel :skull]}
                 :die-1 {:id :die-1
                         :current-face 3
                         :faces [:punch :heal :shields :punches :shovel :skull]}}})))

(deftest set-die-lock
  (is (= (sut/update-game {:dice {:die-0 {:id :die-0
                                          :current-face 1
                                          :faces [:punch :heal :shields :punches :shovel :skull]}
                                  :die-1 {:id :die-1
                                          :current-face 3
                                          :faces [:punch :heal :shields :punches :shovel :skull]}}}
                          [:set-die-lock :die-1 true])
         {:dice {:die-0 {:id :die-0
                         :current-face 1
                         :faces [:punch :heal :shields :punches :shovel :skull]}
                 :die-1 {:id :die-1
                         :current-face 3
                         :locked? true
                         :faces [:punch :heal :shields :punches :shovel :skull]}}})))

(deftest punch-zombie
  (is (= (sut/update-game {:zombies {:zombie-1 {:id :zombie-1 :kind 1 :max-health 5}}}
                          [:punch-zombie :zombie-1 3])
         {:zombies {:zombie-1 {:id :zombie-1
                               :kind 1
                               :max-health 5
                               :damage 3}}}))

  (is (= (sut/update-game {:zombies {:zombie-1 {:id :zombie-1 :kind 1 :max-health 5 :damage 3}}}
                          [:punch-zombie :zombie-1 1])
         {:zombies {:zombie-1 {:id :zombie-1
                               :kind 1
                               :max-health 5
                               :damage 4}}}))

  (is (= (sut/update-game {:zombies {:zombie-1 {:id :zombie-1
                                                :kind 1
                                                :max-health 5
                                                :damage 3}}}
                          [:punch-zombie :zombie-1 2])
         {:zombies {}})))

(deftest reroll
  (is (= (sut/reroll {:player {}
                      :seed 0})
         nil))

  (is (= (sut/reroll {:player {:rerolls 2}
                      :seed 0})
         [[:use-reroll 1]
          [:set-seed 1]]))

  (is (= (sut/reroll {:player {:rerolls 2 :used-rerolls 1}
                      :seed 0})
         [[:use-reroll 2]
          [:set-seed 1]]))

  (is (= (sut/reroll {:player {:rerolls 2 :used-rerolls 2}
                      :seed 0})
         nil))

  (is (= (sut/reroll {:seed 1
                      :player {:rerolls 2}
                      :dice {:die-0 {:id :die-0
                                     :current-face 1
                                     :faces [:punch :heal :shields :punches :shovel :skull]}
                             :die-1 {:id :die-1
                                     :current-face 3
                                     :faces [:punch :heal :shields :punches :shovel :skull]}}})
         [[:use-reroll 1]
          [:reroll-die {:id :die-0 :from 1 :to 3 :roll-id 1}]
          [:reroll-die {:id :die-1 :from 3 :to 2 :roll-id 1}]
          [:set-seed 2]]))

  (is (= (sut/reroll {:seed 1
                      :player {:rerolls 2}
                      :dice {:die-0 {:id :die-0
                                     :current-face 1
                                     :locked? true
                                     :faces [:punch :heal :shields :punches :shovel :skull]}
                             :die-1 {:id :die-1
                                     :current-face 3
                                     :faces [:punch :heal :shields :punches :shovel :skull]}}})
         [[:use-reroll 1]
          [:reroll-die {:id :die-1 :from 3 :to 3 :roll-id 1}]
          [:set-seed 2]])))

(deftest toggle-clamp
  (is (= (sut/toggle-clamp {:dice {:die-0 {:id :die-0
                                           :current-face 1
                                           :faces [:punch :heal :shields :punches :shovel :skull]}
                                   :die-1 {:id :die-1
                                           :current-face 3
                                           :faces [:punch :heal :shields :punches :shovel :skull]}}}
                           :die-1)
         [[:set-die-lock :die-1 true]]))

  (is (= (sut/toggle-clamp {:dice {:die-0 {:id :die-0
                                           :current-face 1
                                           :faces [:punch :heal :shields :punches :shovel :skull]}
                                   :die-1 {:id :die-1
                                           :locked? true
                                           :current-face 3
                                           :faces [:punch :heal :shields :punches :shovel :skull]}}}
                           :die-1)
         [[:set-die-lock :die-1 false]])))

(deftest execute-turn--punch-zombie
  (is (= (sut/use-dice {:zombies {:zombie-1 {:health 5}}}
                       {:target :zombie-1})
         []))

  (is (= (sut/use-dice {:zombies {:zombie-1 {:health 5}}
                        :dice {:die-0 {:id :die-0
                                       :current-face 0
                                       :faces [:punch :heal :shields :punches :shovel :skull]}}}
                       {:target :zombie-1})
         [[:punch-zombie {:target :zombie-1
                          :die-ids [:die-0]
                          :punches 1
                          :health 5}]]))

  (is (= (sut/use-dice {:zombies {:zombie-1 {:health 5}}
                        :dice {:die-0 {:id :die-0
                                       :current-face 5
                                       :faces [:punch :heal :shields :punches :shovel :skull]}}}
                       {:target :zombie-1})
         []))

  (is (= (sut/use-dice {:zombies {:zombie-1 {:health 5}}
                        :dice {:die-0 {:id :die-0
                                       :current-face 3
                                       :faces [:punch :heal :shields :punches :shovel :skull]}
                               :die-1 {:id :die-1
                                       :current-face 0
                                       :faces [:punch :heal :shields :punches :shovel :skull]}}}
                       {:target :zombie-1})
         [[:punch-zombie {:target :zombie-1
                          :die-ids [:die-0 :die-1]
                          :punches 3
                          :health 5}]])))
