(ns graphclj.tools
  (:require [clojure.string :as str]
            [clojure.set :as set]
            [graphclj.centrality :as C]
            [clojure.java.shell :as shell]))

(defn readfile [f]
  "Returns a sequence from a file f"
  (with-open [rdr (clojure.java.io/reader f)]
    (doall (line-seq rdr))))

(defn write-file [to-write]
  (with-open [w (clojure.java.io/writer  "/home/elias/Documents/3I020/graphcljskel/graph.dot" :append false)]
    (.write w to-write)))

(defn rank-nodes [g,l]
  "Ranks the nodes of the graph in relation to label l in accending order. The label l can be either :close or :degree"
  (let [labeledg (if (= l :close)
                   (C/closeness-all g)
                   (C/degrees g))]
    (into {} (sort-by (comp l second) labeledg))))

(defn generate-colors [n]
  (let [step 10]
    (loop [colors {}, current [255.0 160.0 122.0], c 0]
      (if (= c (inc n))
        colors
        (recur (assoc colors c (map #(/ (mod (+ step %) 255) 255) current))
               (map #(mod (+ step %) 255) current) (inc c))))))



(defn to-dot [g]
  "Returns a string in dot format for graph g, each node is colored in relation to its ranking"
  (let [my-g (rank-nodes g :close)
        colors (generate-colors (dec (count (keys g))))]
    (loop [res (str "graph g{\n\t" (loop [res "",colors colors,nodes (keys my-g)]
                                     (if (seq nodes)
                                       (recur (str res (first nodes) " [style=filled color=\"" (apply str (second (first colors))) "\"]\n\t") (rest colors) (rest nodes))
                                       res)))
           updatedg my-g
           g my-g]
      (println res)
      (if (seq g)
        (do (let [[t1 t2] (loop [tmpstr "",ug updatedg, to-link (get (get updatedg (ffirst g)) :neigh)]
                            (if (seq to-link)
                              (recur (str tmpstr (str (ffirst g)) " -- " (str (first to-link)) "\n\t") (update-in ug (vector (first to-link)) #(assoc % :neigh (set/select (fn [x] (not= (ffirst g) x)) (get % :neigh)))) (rest to-link))
                              [tmpstr ug]))]
              (println "\n------------------------------\n" t1 t2)
              (recur (str res t1) t2 (rest g))))
        (str res "}")))))


