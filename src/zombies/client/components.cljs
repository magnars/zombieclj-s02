(ns zombies.client.components
  (:require [clojure.string :as str]
            [dumdom.core :refer [defcomponent]]))

(defcomponent Zombie [{:keys [id kind max-health health punches falling?]}]
  [:div.zombie-position
   [:div.zombie {:class (str "zombie-" kind
                             (when falling? " falling")
                             " " (str/join " " punches))
                 :on-click [:send-command [:use-dice {:target id}]]}
    [:div.zombie-health
     (for [i (range max-health)]
       [:div.heart (when (<= health i)
                     {:class "lost"})])]
    [:div.zombie-punches
     [:div.zombie-punch-1]
     [:div.zombie-punch-2]
     [:div.zombie-punch-3]
     [:div.zombie-punch-4]
     [:div.zombie-punch-5]]]])

(defcomponent Tip [{:keys [position header prose action]}]
  [:div.tip {:on-click action}
   [:div.tip-box {:class (name position)}
    [:div.tip-arrow]
    [:div.tip-header header]
    [:div.tip-prose prose]]])

(defcomponent Player [{:keys [max-health]}]
  [:div
   [:div.player-health
    (for [_ (range max-health)]
      [:div.heart])]])

(defcomponent Die [{:keys [id roll-id current-face previous-face faces locked? status]}]
  [:div.die-w-lock
   [:div.die {:key (str id roll-id)
              :class (str (name id)
                          (cond
                            (= :used status) " used"
                            (= :using status) " using"
                            previous-face " rolling"
                            :else " entering"))}
    [:div.cube {:class (if previous-face
                         (str "roll-" previous-face "-to-" current-face)
                         (str "entering-" current-face))}
     (map (fn [face i]
            [:div.face {:class (str "face-" i " " (name face))}])
          faces
          (range))]]
   [:div.clamp {:class (when locked? "locked")
                :on-click [:send-command [:toggle-clamp id]]}
    [:div.lock
     [:div.padlock]]]])

(defcomponent Rerolls [{:keys [rerolls used-rerolls]}]
  [:div.rerolls {:on-click [:send-command [:reroll]]}
   (let [num (or rerolls 0)
         used (or used-rerolls 0)]
     (for [i (range num)]
       [:div.reroll {:class (when (< i used) "used")}]))])

(defcomponent Page [{:keys [zombies tip player dice server-error]}]
  (if server-error
    [:h1 server-error]
    [:div.page
     [:div.surface
      [:div.skyline
       (for [i (range 16)]
         [:div.building {:class (str "building-" i)}])]
      [:div.zombies
       (map Zombie (vals zombies))]
      (when player (Player player))
      [:div.dice-row
       (->> (map Die (vals dice))
            (interpose [:div.dice-spacing]))
       (Rerolls player)]]
     (when tip (Tip tip))]))
