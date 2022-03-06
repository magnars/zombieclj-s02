(ns zombies.client.actions
  (:require [cljs.core.async :refer [<! timeout]]
            [clojure.core.match :refer [match]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defn perform-actions [store actions]
  (go
    (loop [remaining-actions (seq actions)]
      (when remaining-actions
        (prn (first remaining-actions))
        (match (first remaining-actions)
          [:assoc-in path v] (do (swap! store assoc-in path v)
                                 (recur (next remaining-actions)))
          [:show-tips tips] (swap! store assoc-in [:tips]
                                   (assoc tips :action [:perform-actions (into [[:assoc-in [:tips] nil]]
                                                                               (next remaining-actions))]))
          [:wait ms] (do (<! (timeout ms))
                         (recur (next remaining-actions))))))))
