(ns zombies.client.components
  (:require [dumdom.core :refer [defcomponent]]))

(defcomponent Zombie [{:keys [id kind max-health]}]
  [:div.zombie-position
   [:div.zombie {:class (str "zombie-" kind)}
    [:div.zombie-health
     (for [_ (range max-health)]
       [:div.heart])]]])

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

(defcomponent Die [{:keys [id roll-id current-face previous-face faces]}]
  [:div.die-w-lock
   [:div.die {:key (str id roll-id)
              :class (str (name id)
                          (if previous-face
                            " rolling"
                            " entering"))}
    [:div.cube {:class (if previous-face
                         (str "roll-" previous-face "-to-" current-face)
                         (str "entering-" current-face))}
     (map (fn [face i]
            [:div.face {:class (str "face-" i " " (name face))}])
          faces
          (range))]]])

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
