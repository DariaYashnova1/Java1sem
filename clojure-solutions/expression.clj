(defn op [f]
      (fn [& args]
          (fn [var]
              (apply f (map #(% var) args)))))


(def constant constantly)
(defn variable [name] #(% name))

(def add (op +))
(def subtract (op -))
(def multiply (op *))
(def negate (op -))

(def divide (op (fn([x] (/ 1 (double x)))
                   ([st & x] (/ (double st) (reduce * x))))))



(defn square [n] (* n n))

(defn domean
      [& args] (/ (apply + args) (count args))
      )

(def mean (op domean))
(def varn
  (op (
        fn [& a] (let [mean (apply domean a)]
                      ( / (reduce + (mapv #(square (- % mean))  a))
                          (count a)
                          )
                      )
           )
      )
  )

(def ops {'+      add
          'negate negate
          'varn    varn
          '-      subtract
          '*      multiply
          '/      divide
          'mean    mean})


(defn parse [str]
      (cond (number?  str) (constant str)
            (symbol? str) (variable (name str))
            :else (apply (ops (first str)) (mapv parse (rest str)))))

(defn parseFunction [str]
      (parse (read-string str)))
