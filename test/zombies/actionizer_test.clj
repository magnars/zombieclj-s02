(ns zombies.actionizer-test
  (:require [clojure.test :refer [deftest is testing]]
            [zombies.actionizer :as sut]))

(deftest add-zombie
  (is (= (sut/event->actions [:add-zombie {:id :zombie-1 :kind 1 :max-health 5}])
         [[:assoc-in [:zombies :zombie-1] {:id :zombie-1
                                           :kind 1
                                           :max-health 5}]])))

(deftest show-tip
  (is (= (sut/event->actions [:show-tip {:position :at-zombies
                                         :header "Zombiene kommer!"
                                         :prose "Det er zombier overalt."}])
         [[:show-tip {:position :at-zombies
                      :header "Zombiene kommer!"
                      :prose "Det er zombier overalt."}]])))

(deftest set-player-health
  (is (= (sut/event->actions [:set-player-health 9])
         [[:assoc-in [:player :max-health] 9]])))

(deftest set-player-rerolls
  (is (= (sut/event->actions [:set-player-rerolls 9])
         [[:assoc-in [:player :rerolls] 9]])))

(deftest add-die
  (is (= (sut/event->actions [:add-dice [{:id :die-0
                                          :current-face 2
                                          :faces [:punch :heal :shields :punches :shovel :skull]}
                                         {:id :die-1
                                          :current-face 0
                                          :faces [:punch :heal :shields :punches :shovel :skull]}]])
         [[:assoc-in [:dice :die-0] {:id :die-0
                                     :current-face 2
                                     :faces [:punch :heal :shields :punches :shovel :skull]}]
          [:assoc-in [:dice :die-1] {:id :die-1
                                     :current-face 0
                                     :faces [:punch :heal :shields :punches :shovel :skull]}]
          [:wait 2000]])))

(deftest use-reroll
  (is (= (sut/event->actions [:use-reroll 1])
         [[:assoc-in [:player :used-rerolls] 1]])))

(deftest reroll-die
  (is (= (sut/event->actions [:reroll-die {:id :die-0, :roll-id 1, :from 2, :to 0}])
         [[:assoc-in [:dice :die-0 :roll-id] 1]
          [:assoc-in [:dice :die-0 :current-face] 0]
          [:assoc-in [:dice :die-0 :previous-face] 2]])))

(deftest toggle-clamp
  (is (= (sut/event->actions [:set-die-lock :die-0 true])
         [[:assoc-in [:dice :die-0 :locked?] true]])))

(deftest punch-zombie
  (is (= (sut/event->actions [:punch-zombie {:target :zombie-1
                                             :die-ids [:die-3]
                                             :punches 1
                                             :health 5}])
         [[:assoc-in [:dice :die-3 :status] :using]
          [:assoc-in [:zombies :zombie-1 :punches] ["punched-1"]]
          [:assoc-in [:zombies :zombie-1 :health] 4]
          [:wait 200]
          [:assoc-in [:dice :die-3 :status] :used]]))

  (is (= (sut/event->actions [:punch-zombie {:target :zombie-1
                                             :die-ids [:die-3]
                                             :punches 2
                                             :health 5}])
         [[:assoc-in [:dice :die-3 :status] :using]
          [:assoc-in [:zombies :zombie-1 :punches] ["punched-1"]]
          [:assoc-in [:zombies :zombie-1 :health] 4]
          [:wait 200]
          [:assoc-in [:zombies :zombie-1 :punches] ["punched-4"]]
          [:assoc-in [:zombies :zombie-1 :health] 3]
          [:wait 200]
          [:assoc-in [:dice :die-3 :status] :used]]))

  (is (= (sut/event->actions [:punch-zombie {:target :zombie-1
                                             :die-ids [:die-3]
                                             :punches 2
                                             :health 2}])
         [[:assoc-in [:dice :die-3 :status] :using]
          [:assoc-in [:zombies :zombie-1 :punches] ["punched-1"]]
          [:assoc-in [:zombies :zombie-1 :health] 1]
          [:wait 200]
          [:assoc-in [:zombies :zombie-1 :punches] ["punched-4"]]
          [:assoc-in [:zombies :zombie-1 :health] 0]
          [:wait 200]
          [:assoc-in [:zombies :zombie-1 :falling?] true]
          [:assoc-in [:dice :die-3 :status] :used]])))
