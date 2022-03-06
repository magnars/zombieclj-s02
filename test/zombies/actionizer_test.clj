(ns zombies.actionizer-test
  (:require [clojure.test :refer [deftest is testing]]
            [zombies.actionizer :as sut]))

(deftest add-zombie
  (is (= (sut/event->actions [:add-zombie {:id :zombie-1 :kind 1 :max-health 5}])
         [[:assoc-in [:zombies :zombie-1] {:id :zombie-1
                                           :kind 1
                                           :max-health 5}]])))

(deftest show-tips
  (is (= (sut/event->actions [:show-tips {:position :at-zombies
                                          :header "Zombiene kommer!"
                                          :prose "Det er zombier overalt."}])
         [[:wait 1000]
          [:show-tips {:position :at-zombies
                       :header "Zombiene kommer!"
                       :prose "Det er zombier overalt."}]])))

(deftest set-player-health
  (is (= (sut/event->actions [:set-player-health 9])
         [[:assoc-in [:player] {:max-health 9}]])))

(deftest add-die
  (is (= (sut/event->actions [:add-die {:id :die-0
                                        :current-face :shields
                                        :faces [:punch :heal :shields :punches :shovel :skull]}])
         [[:assoc-in [:dice :die-0] {:id :die-0
                                     :entering? true
                                     :current-face :shields
                                     :faces [:punch :heal :shields :punches :shovel :skull]}]])))
