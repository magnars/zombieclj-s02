(ns ^:figwheel-hooks zombies.dev
  (:require [gadget.inspector :as inspector]
            [zombies.client.main :as main]))

(defn ^:after-load render []
  (main/render))

(inspector/inspect "App state" main/store)
