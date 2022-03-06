(ns zombies.system
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [compojure.core :refer [GET routes]]
            [compojure.route :as route]
            [integrant.core :as ig]
            [org.httpkit.server :as server]))

(def system
  {:app/config {}
   :app/handler {}
   :adapter/http-kit {:config (ig/ref :app/config)
                      :handler (ig/ref :app/handler)}})

(defmethod ig/init-key :app/config [_ _]
  (edn/read-string (slurp (io/resource "config.edn"))))

(defmethod ig/init-key :app/handler [_ _]
  (routes
   (GET "/" [] (io/resource "public/index.html"))
   (route/resources "/")))

(defmethod ig/init-key :adapter/http-kit [_ {:keys [config handler]}]
  (let [stop-fn (server/run-server handler {:port (:port config)})]
    (println "Started server on" (:port config))
    stop-fn))

(defmethod ig/halt-key! :adapter/http-kit [_ stop-fn]
  (stop-fn))
