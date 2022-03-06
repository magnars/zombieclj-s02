(ns zombies.client.actions
  (:require [cljs.core.async :refer [<! timeout]]
            [clojure.core.match :refer [match]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defn perform-actions [store actions]
  (go
    (loop [[action & next-actions] actions]
      (when action
        (prn action)
        (match action
          [:assoc-in path v] (do (swap! store assoc-in path v)
                                 (recur next-actions))
          [:show-tips tips] (let [id (some-> (:id tips) name)]
                              (if (and id (js/localStorage.getItem id))
                                (recur next-actions)
                                (do
                                  (swap! store assoc-in [:tips]
                                         (assoc tips :action [:perform-actions (into [[:assoc-in [:tips] nil]]
                                                                                     next-actions)]))
                                  (when id (js/localStorage.setItem id "seen")))))
          [:wait ms] (do (<! (timeout ms))
                         (recur next-actions)))))))
