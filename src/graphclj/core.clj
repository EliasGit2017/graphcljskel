(ns graphclj.core
  (:require [graphclj.graph :as graph]
            [graphclj.tools :as tools]
            [graphclj.centrality :as central]
            [clojure.java.shell :as shell])
  (:gen-class))

(defn -main
  [& args]
  (print "test")
;; Commandes système pour les test et l'affichage des graphes
  (shell/sh "rm" "-f" "view.ps" "graph.dot")
  (write-file (to-dot (graph/gen-graph (readfile "/home/elias/Documents/3I020/graphcljskel/enron_static.csv"))))
  (shell/sh "dot" "-Tpdf" "/home/elias/Documents/3I020/graphcljskel/graph.dot" "-o" "enron_static.pdf")
  (shell/sh "xdg-open" "view.ps"))
