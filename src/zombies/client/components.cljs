(ns zombies.client.components
  (:require [dumdom.core :refer [defcomponent]]
            [zombies.client.event-bus :as bus]))

(defcomponent Zombie [{:keys [id kind max-health]}]
  [:div.zombie-position
   [:div.zombie {:className (str "zombie-" kind)}
    [:div.zombie-health
     (for [_ (range max-health)]
       [:div.heart])]]])

(defcomponent Tip [{:keys [position header prose action]}]
  [:div.tip {:onClick #(when action (bus/publish action))}
   [:div.tip-box {:className (name position)}
    [:div.tip-arrow]
    [:div.tip-header header]
    [:div.tip-prose prose]]])

(defcomponent Player [{:keys [max-health]}]
  [:div
   [:div.player-health
    (for [_ (range max-health)]
      [:div.heart])]])

(defcomponent Die [{:keys [id current-face faces entering?]}]
  [:div.die-w-lock
   [:div.die {:className (str (name id)
                              (when entering? " entering"))}
    [:div.cube {:className (str "facing-" (.indexOf faces current-face))}
     (map (fn [face i]
            [:div.face {:className (str "face-" i " " (name face))}])
          faces
          (range))]]])

(defcomponent Rerolls [{:keys [rerolls used-rerolls]}]
  [:div.rerolls {:onClick #(bus/publish [:send-command [:reroll]])}
   (let [num (or rerolls 0)
         used (or used-rerolls 0)]
     (for [i (range num)]
       [:div.reroll {:className (when (< i used) "used")}]))])

(defcomponent Page [{:keys [zombies tip player dice server-error]}]
  (if server-error
    [:h1 server-error]
    [:div.page
     [:div.surface
      [:div.skyline
       (for [i (range 16)]
         [:div.building {:className (str "building-" i)}])]
      [:div.zombies
       (map Zombie (vals zombies))]
      (when player (Player player))
      [:div.dice-row
       (->> (map Die (vals dice))
            (interpose [:div.dice-spacing]))
       (Rerolls player)]]
     (when tip (Tip tip))]))
