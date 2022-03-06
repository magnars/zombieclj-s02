(ns zombies.client.event-bus)

(defonce listeners (atom {}))

(defn watch [id topic f]
  (swap! listeners assoc id {:topic topic :f f}))

(defn publish [[topic & args]]
  (prn topic args)
  (doseq [listener (vals @listeners)]
    (when (= topic (:topic listener))
      (apply (:f listener) args))))
