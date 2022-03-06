(ns dev
  (:require [figwheel.main]
            [figwheel.main.api]
            [integrant.repl :as repl :refer [reset]]
            [zombies.system :as system]))

(defn start []
  (set! *print-namespace-maps* false)
  (repl/set-prep! (constantly system/system))
  (repl/go))

(defn stop []
  (integrant.repl/halt))

(defn cljs []
  (if (get @figwheel.main/build-registry "dev")
    (figwheel.main.api/cljs-repl "dev")
    (figwheel.main.api/start "dev")))
