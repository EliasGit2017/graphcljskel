(ns graphclj.centrality
  (:require [graphclj.graph :as graph]
            [clojure.set :as set]))



;; Remplacer par quelque chose de meilleur en taille de code et complexité.
(defn degrees [g]
  "Calculates the degree centrality for each node"
  (loop [my-g g
         res g]
    (if (seq my-g)
      (recur (rest my-g) (assoc-in res (vector (ffirst my-g) :degree) (count (first (vals (second (first my-g)))))))
      res)))

(defn distance [g n]
  "Calculate the distances of one node to all the others")

(defn closeness [g n]
  "Returns the closeness for node n in graph g")


(defn closeness-all [g]
  "Returns the closeness for all nodes in graph g")


