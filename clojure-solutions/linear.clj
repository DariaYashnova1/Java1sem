
(defn sizeChecker [elements]
      (every? (fn [element] (== (count element) (count (first elements)))) elements))

(defn vectorsChecker [vectors]
      (every? (fn [x] (and (vector? x) (every? number? x))) vectors))


(defn matrixChecker [matrices]
      (every? (fn [x] (and (vector? x)
                           (and (vectorsChecker x) (sizeChecker x)))) matrices))

(defn oneSimplex [sim]
      (if
        (vectorsChecker (vector sim)) true
                                      (every? true? (mapv #(== (count %1) %2) sim (rseq (vec (range 1 (+ (count sim) 1))
                                                                                             )
                                                                                        )
                                                          )
                                              )
                                      )
      )
(defn simplexChecker [sims]
      (and (every? true? (mapv #(oneSimplex %) sims)) (sizeChecker sims))
      )
(defn vecOp [f & vectors]
      {:pre [(and (vectorsChecker vectors)
                  (sizeChecker vectors))]
       :post [(sizeChecker (conj vectors %) )]}
      (apply mapv f vectors))

(defn matOp [f & matrices]
      {:pre [(matrixChecker matrices)
             (sizeChecker matrices)]
       :post [(sizeChecker (conj matrices %) )]
       }
      (apply mapv f matrices))

(defn symOp [f]
      (fn rf [& vectors]
          {:pre [(simplexChecker vectors)]
           :post [(oneSimplex %)]}
          (if (vectorsChecker vectors)
            (apply mapv f vectors)
            (apply mapv rf vectors)
            )
          )
      )
(def x+ (symOp +))
(def x- (symOp -))
(def x* (symOp *))
(def xd (symOp /))

(def v+ (partial vecOp +))
(def v- (partial vecOp -))
(def v* (partial vecOp *))
(def vd (partial vecOp /))

(def m+ (partial matOp v+))
(def m- (partial matOp v-))
(def m* (partial matOp v*))
(def md (partial matOp vd))


(defn scalar [& vectors]
      {:pre [(and (vectorsChecker vectors)
                  (sizeChecker vectors))]
       :post [(number? %)]
       }
      (apply + (apply v* vectors)))


(defn transpose [matrix]
      (apply mapv vector matrix))

(defn v*s [onevector & s]
      {:pre [(every? number? s)]
        :post [(sizeChecker (vector onevector (vec %)))]
       }
      (mapv (fn [x] (* x (apply * s))) onevector))

(defn m*s [matrix & s]
      {:pre [(every? number? s)]
       :post [(sizeChecker (vector matrix (vec %)))]
       }
      (mapv (fn [x] (apply v*s x s)) matrix))

(defn m*v [matrix & v]
      {
       :post [(sizeChecker (vector matrix (vec %)))]
       }
      (mapv (fn [x] (apply scalar x v)) matrix))



(defn m*m [& matrices]
      {:pre [(matrixChecker matrices)]}
      (reduce (fn [x y]
                  (mapv (fn [z] (m*v (transpose y) z)) x))
              matrices))

(defn disSubtract [x y v1 v2]
      (- (* (nth x v1) (nth y v2))
         (* (nth y v1) (nth x v2))))

(defn vect [& vectors]
      {:pre [(and (vectorsChecker vectors) (sizeChecker vectors))]
       :post [(sizeChecker (conj vectors %))]
       }
      (reduce (fn [x y]
                  (vector (disSubtract x y 1 2)
                          (disSubtract x y 2 0)
                          (disSubtract x y 0 1)))
              vectors))