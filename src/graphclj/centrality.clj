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


;; Essai avec Parcours en largeur simple (Car les coûts entre les noeuds sont identiques)
(defn distance [g n]
  "Calculate the distances of one node to all the others"
  (loop [d (assoc (into [] (take (apply max (keys g)) (repeat -1))) n 0)
         F [n]]
    (if (seq F)
      (do (let [x (first F)
                [t1 t2] (loop [succ (get (get g x) :neigh)
                               d d
                               tmp (into [] (rest F))]
                          (if (seq succ)
                            (if (= (nth d (first succ)) -1)
                              (do (println "tableau tmp:" tmp (first succ) d)
                                  (recur (rest succ) (assoc d (first succ) (inc (nth d x))) (conj tmp (first succ))))
                              (recur (rest succ) d tmp))
                            [d tmp]))]
            (recur t1 t2)))
      (zipmap (range (count d)) d))))


(defn closeness [g n]
  "Returns the closeness for node n in graph g"
  (let [d (distance g n)]
    (float (reduce + (mapv #(/ 1 %) (filter #(> % 0) (vals (distance g n))))))))


(defn closeness-all [g]
  "Returns the closeness for all nodes in graph g"
(loop [my-g g
         res g]
    (if (seq my-g)
      (recur (rest my-g) (assoc-in res (vector (ffirst my-g) :close) (closeness g (ffirst my-g))))
      res)))
