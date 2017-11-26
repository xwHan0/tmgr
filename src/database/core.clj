(ns database.core
  (:require [clojure.java.jdbc :refer :all]))

;
(def generated-key (keyword "last_insert_rowid()"))
(defn insert-db [db table content]
  (generated-key (first (insert! db table content))))

; (def db {:connection-uri "jdbc:sqlite:resources/database/tmgr.sqlite"})  ;For sqlite-jdbc "3.15.1"

(def db
  {
    :classname   "org.sqlite.JDBC"
    :subprotocol "sqlite"
    :subname     "resources/database/tmgr.sqlite"
  })  

(defn read-task [tid]
  (let [
    sql (str "SELECT x.id, x.title, y.owner, y.content FROM tasks x, descriptions y WHERE 
             x.id = y.tid and x.id = " tid)
    items (query db [sql])
    ]
    (first items)))

(defn read-task-status [tid]
  (let [
    pred (str "SELECT x.complete, x.status, y.content FROM status x, descriptions y WHERE x.cid = y.id and x.tid = " tid )
    items (query db [pred])]
    (-> items last)))

(defn read-sub-tasks [tid]
  (let [
    sql (str "SELECT x.id,x.title,y.owner,y.content FROM status x, descriptions y WHERE ")
    sql (str sql "x.id = y.tid and x.pid = " tid)
    ]
    (query db [sql])))

(defn add-task [{:keys [pid status due title] :as task}]
  (insert! db :tasks task))

(defn add-comment [{:keys [tid type owner content duration] :as desc}]
  (let [description (insert! db :descriptions desc)
        did (generated-key (first description))
        comment (insert! db :comments {:tid tid :cid did :type 0})
        cid (generated-key (first comment))]
    ; (update! db :tasks {:cid cid} ["id = ?" tid])
    ))
    
(defn add-status [{:keys [tid complete status description]}]
  (let [did (insert-db db :descriptions {:tid tid :content description})
        sid (insert-db db :status {:tid tid :cid did :complete complete :status status})]
    (str sid)))