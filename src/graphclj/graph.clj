(ns graphclj.graph
  (:require [clojure.string :as str]
            [graphclj.tools :as tools]))

;; Generate a graph from the lines
(defn gen-graph [lines]
  "Returns a hashmap contating the graph"
  (loop [my-lines lines
         res {}]
    (if (seq my-lines)
      (recur (rest my-lines) (do (let [a (Integer/parseInt (first (str/split (first my-lines) #" ")))
                                       b (Integer/parseInt (second (str/split (first my-lines) #" ")))]
                                   (if-not (contains? res a)
                                     (assoc res a {:neigh (set (list b))})
                                     (update-in res (vector a) #(assoc % :neigh (conj (get % :neigh) b))))
                                   (if-not (contains? res b)
                                     (assoc res b {:neigh (set (list a))})
                                     (update-in res (vector b) #(assoc % :neigh (conj (get % :neigh) a)))))))
      res)))

(defn erdos-renyi-rnd [n,p]
  "Returns a G_{n,p} random graph, also known as an Erdős-Rényi graph"
  (loop [res {}]
    (for [i (range n)]
      (assoc res i {:neigh (set (random-sample p (range n)))}))))

;; random-sample -----> The output of random-sample is a sequence.
;; Each element of the original collection has probability "prob"
;; of being included in the output sequence.