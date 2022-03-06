(ns dev
  (:require [integrant.repl :as repl :refer [reset]]
            [zombies.system :as system]))

(defn start []
  (set! *print-namespace-maps* false)
  (repl/set-prep! (constantly system/system))
  (repl/go))

(defn stop []
  (integrant.repl/halt))
