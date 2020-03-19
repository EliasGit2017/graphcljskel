(ns graphclj.core
  (:require [graphclj.graph :as graph]
            [graphclj.tools :as tools]
            [graphclj.centrality :as central]
            [clojure.java.shell :as shell])
  (:gen-class))

(defn -main
  [& args]
  (println "\t ---------------------------graphcljhskel-------------------------------")
  (println "Lancement des tests des différentes fonctions permettant de modéliser un graphe :")
;; Commandes systeme en shell pour les tests et l'affichage du graphe. On utilise Pygraphviz pour tracer le graphe à l'aide de la commande :
;; $ dot -Tpdf /***PATH***/graphcljskel/graph.dot -o enron_static.pdf
;; Le graphe résultat pour le jeu de données enron_static.csv est présent sous le nom : enron_static.pdf
  (println "===>(1) Tester le programme avec un graphe aléatoire selon le modèle Erdős–Rényi :")
  (println "===>(2) Tester le programme avec le jeu de données enron_static.csv")
  (loop [cmd (read-line)
         bool false]
    (shell/sh "rm" "-f" "view.ps" "graph.dot") ;; Suppression des fichiers de même nom déjà existants
    (if-not bool
      (cond
        (= cmd (str 1)) (do (println "===> Choisir le nombre de noeuds n puis la probabilité p :")
                            (let [n (read-line)
                                  p (read-line)]
                              (println "Génération du graphe et affichage du résultat sous forme pdf :")
                              (tools/write-file (tools/to-dot (graph/erdos-renyi-rnd (Integer/parseInt n) (Float/parseFloat p)))))
                            (shell/sh "dot" "-Tpdf" "/home/elias/Documents/3I020/graphcljskel/graph.dot" "-o" "view.pdf")
                            (shell/sh "xdg-open" "view.pdf")
                            (recur 0 true))
        (= cmd (str 2)) (do (println "Génération du graphe à partir de enron_static.csv et affichage du résultat en pdf :")
                            (tools/write-file (tools/to-dot (graph/gen-graph (tools/readfile "/home/elias/Documents/3I020/graphcljskel/enron_static.csv"))))
                            (shell/sh "dot" "-Tpdf" "/home/elias/Documents/3I020/graphcljskel/graph.dot" "-o" "enron_static.pdf")
                            (shell/sh "xdg-open" "enron_static.pdf")
                            (recur 0 true))
        :else (do (println "Erreur veuillez choisir un argument correct (1 ou 2)")
                  (recur (read-line) false))))))