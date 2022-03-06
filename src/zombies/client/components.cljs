(ns zombies.client.components
  (:require [dumdom.core :refer [defcomponent]]
            [zombies.client.event-bus :as bus]))

(defcomponent Zombie [{:keys [id kind max-health]}]
  [:div.zombie-position
   [:div.zombie {:className (str "zombie-" kind)}
    [:div.zombie-health
     (for [_ (range max-health)]
       [:div.heart])]]])

(defcomponent Tips [{:keys [position header prose action]}]
  [:div.tips {:onClick #(when action (bus/publish action))}
   [:div.tips-box {:className (name position)}
    [:div.tips-arrow]
    [:div.tips-header header]
    [:div.tips-prose prose]]])

(defcomponent Player [{:keys [max-health]}]
  [:div
   [:div.player-health
    (for [_ (range max-health)]
      [:div.heart])]])

(defcomponent Page [{:keys [zombies tips player]}]
  [:div.page
   [:div.surface
    [:div.skyline
     (for [i (range 16)]
       [:div.building {:className (str "building-" i)}])]
    [:div.zombies
     (for [zombie (vals zombies)]
       [Zombie zombie])]
    (when player (Player player))]
   (when tips (Tips tips))])
