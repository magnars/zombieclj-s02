(ns zombies.client.components
  (:require [dumdom.core :refer [defcomponent]]))

(defcomponent Page [data]
  [:div "Zombies!"])
