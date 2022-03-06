(ns zombies.client.components
  (:require [dumdom.core :refer [defcomponent]]))

(defcomponent Zombie [{:keys [id kind max-health]}]
  [:div.zombie-position
   [:div.zombie {:className (str "zombie-" kind)}
    [:div.zombie-health
     (for [i (range max-health)]
       [:div.heart])]]])

(defcomponent Page [{:keys [zombies]}]
  [:div.page
   [:div.surface
    [:div.skyline
     (for [i (range 16)]
       [:div.building {:className (str "building-" i)}])]
    [:div.zombies
     (for [zombie (vals zombies)]
       [Zombie zombie])]]])
