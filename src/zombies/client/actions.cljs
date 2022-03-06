(ns zombies.client.actions
  (:require [clojure.core.match :refer [match]]))

(defn perform-actions [store actions]
  (loop [remaining-actions (seq actions)]
    (when remaining-actions
      (match (first remaining-actions)
        [:assoc-in path v] (do (swap! store assoc-in path v)
                               (recur (next actions)))))))
