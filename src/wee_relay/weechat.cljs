(ns wee-relay.weechat
  (:require cljsjs.pako))

(declare get-data-for-type)

(defn get-slice [data index length]
  (if (> (+ @index length) (.-byteLength data))
    nil
    (let [slice (.slice data @index (+ @index length))]
      (reset! index (+ @index length))
      slice)))

(defn read-and-shift [array index shift-amount]
  (-> (nth array index)
      (bit-and 0xff)
      (bit-shift-left shift-amount)))

(defn get-integer
  "Parse four bytes from the input data and bitwise shift them in to
  a 32bit integer"
  [data index]
  (let [parsed-data (-> (js/Uint8Array. (get-slice data index 4))
                        array-seq)
        first-byte (read-and-shift parsed-data 0 24)
        second-byte (read-and-shift parsed-data 1 16)
        third-byte (read-and-shift parsed-data 2 8)
        fourth-byte (read-and-shift parsed-data 3 0)]
    (-> (bit-or first-byte second-byte)
        (bit-or third-byte)
        (bit-or fourth-byte))))

(defn get-byte [data index]
  (-> (js/Uint8Array. (get-slice data index 1))
      array-seq
      first))

(defn get-header [data index]
  (let [length (get-integer data index)
        compression (get-byte data index)]
    {:length length
     :compression compression}))

(defn uint-to-string [array]
  (->> (array-seq array)
       (map (fn [character] (char character)))
       (clojure.string/join)))

(defn get-string [data index]
  (let [length (get-integer data index)]
    (if (> length 0)
      (-> (get-slice data index length)
          js/Uint8Array.
          uint-to-string)
      "")))

(defn get-char [data index]
  (char (get-byte data index)))

(defn number->str [data index]
  (let [length (get-byte data index)]
    (-> (get-slice data index length)
         js/Uint8Array.
         uint-to-string)))

(defn get-type [data index]
  (let [type-data (get-slice data index 3)]
    (if-not (nil? type-data)
      (uint-to-string type-data)
      nil)))

(defn get-array [data index]
  (let [type (get-type data index)
        count (get-integer data index)]
    (if (= count 0)
      nil
      (loop [array [(get-data-for-type data index type)]
             count (dec count)]
        (if (> count 0)
          (recur (conj array (get-data-for-type data index type))
                 (dec count))
          array)))))

(defn get-info [data index]
  (let [name (get-string data index)
        value (get-string data index)]
    {:name name
     :value value}))

(defn get-item [data index]
  (let [count (get-integer data index)]
    (loop [count count
           results {}]
      (if (> count 0)
        (let [name (get-string data index)
              type (get-type data index)
              value (get-data-for-type data index type)]
          (recur (dec count)
                 (merge results {(keyword name) value})))
        results))))

(defn get-infolist [data index]
  (let [name (get-string data index)
        count (get-integer data index)
        items (if (> count 0)
                (loop [results [(get-item data index)]
                       count (dec count)]
                  (if (> count 0)
                    (recur (conj results (get-item data index))
                           (dec count))
                    results))
                nil)]
    {:name name
     :items items}))

(defn get-map [data index]
  (let [key-type (get-type data index)
        value-type (get-type data index)
        count (get-integer data index)]
    (loop [result {}
           count count]
      (if (> count 0)
        (recur (merge result {(get-data-for-type data index key-type)
                              (get-data-for-type data index value-type)})
               (dec count))
        result))))

(defn get-hdata [data index]
  (let [h-path (clojure.string/split (get-string data index) "/")
        keys (->> (clojure.string/split (get-string data index) ",")
                  (map (fn [key] (clojure.string/split key ":"))))
        count (get-integer data index)]
    (loop [results []
           count count]
      (if (> count 0)
        (recur (conj results (merge
                               {:pointers
                                (doall (map (fn [_] (number->str data index)) h-path))}
                               (doall (into {} (map (fn [key-type]
                                                      {(keyword (nth key-type 0))
                                                       (get-data-for-type data index (nth key-type 1))})
                                                    keys)))))
               (dec count))
        results))))

(defn get-data-for-type [data index type]
  (case type
    "chr" (get-char data index)
    "int" (get-integer data index)
    "str" (get-string data index)
    "lon" (number->str data index)
    "buf" (get-string data index)
    "ptr" (str "0x" (number->str data index))
    "tim" (-> (number->str data index)
              js/parseInt
              (* 1000)
              js/Date.)
    "arr" (get-array data index)
    "inf" (get-info data index)
    "inl" (get-infolist data index)
    "htb" (get-map data index)
    "hda" (get-hdata data index)))

(defn get-object [data index]
  (let [type (get-type data index)]
    (if (nil? type)
      nil
      (get-data-for-type data index type))))

(defn get-objects [data index]
  (loop [object (get-object data index)
         parsed-data []]
    (if-not (nil? object)
      (do
        (recur (get-object data index)
               (conj parsed-data object)))
      parsed-data)))

(defn decode-data [data]
  (let [index (atom 0)
        {:keys [length compression] :as header} (get-header data index)
        data (if (= compression 1)
               (let [decompressed-data (.inflate js/pako (get-slice data index (- length @index)))]
                 (reset! index 0)
                 decompressed-data)
               (js/Uint8Array. data))
        id (get-string data index)
        data (get-objects data index)]
    {:id id
     :data data}))
