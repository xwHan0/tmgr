(ns view.milestone
  (:require
    [clojure.core.ex :refer :all]
    [clj-time.core :as t]
    [clj-time.format :as tf]
    [view.util :as util]))

(def task-format (tf/formatter "yyyy-MM-dd"))

(defn- parse-date [date] (tf/parse task-format date))

(defn- in-days [start finish]
  (let [sec (t/in-minutes (t/interval start finish))]
    (/ sec 60 24)))

(defn- milestone-x [date start due size offset]
  (let [
    dates (in-days start date)
    len (* (/ dates due) size)
  ]
    (+ offset len)))



(defn- svg-milestone [{:keys [x date title]}]
  [:g 
    [:text {:y 36}
      [:tspan {:x (- x 25) :dy 0} date]
      [:tspan {:x (- x 5) :dy 20} title]]
    [:use {:xlink:href "#milestone" :x x :y 60}]])

(defn- sym-dcp []
  (let []
    [:g#dcp
      [:line {:x1 20 :y1 0 :x2 20 :y2 150 :stroke "blue" }]
      [:path {:d "M20 150 L40 170 L30 170 L30 230 L10 230 L10 170 L0 170 Z"}]]))

(defn- milestone-svg []
  (let [
    start "2017-11-1"
    finish "2018-5-30"
    st (parse-date start)
    ed (parse-date finish)
    mx (milestone-x (parse-date "2018-1-30") st (in-days st ed) 1000 50)
  ] 
    [:svg {:width 1100 :height 400 :xmlns "http://www.w3.org/2000/svg" :xmlns:xlink "http://www.w3.org/1999/xlink"}
      [:defs
        [:linearGradient#orange_red {:x1 "0%" :y1 "0%" :x2 "100%" :y2 "0%"}
          [:stop {:offset "0%" :style "stop-color:rgb(255,255,0);stop-opacity:1"}]
          [:stop {:offset "100%" :style "stop-color:rgb(255,0,0);stop-opacity:1"}]]
        [:g#milestone
          [:path {:d "M0 0 L10 0 L5 20 Z" :fill "red"}]]
        (sym-dcp)]
      [:path {:d "M50 60 L350 80 L1050 80 L1050 60 L1080 100 L1050 140 L1050 120 L350 120 L50 140 Z" :fill "url(#orange_red)" :stroke "black"}]
      (svg-milestone {:x mx :date "2017-12-01" :title "TR1"})
      (svg-milestone {:x 350 :date "2017-12-01" :title "TR1"})
      (svg-milestone {:x 650 :date "2017-12-01" :title "TR1"})
      [:use {:xlink:href "#dcp" :x 100 :y 60}]
      ]))

(defn page []
  (let [
        ]
    (util/page
      ;JS
      []
      ;CSS
      ["css/milestone.css"]
      ;Component
      [:h2 "Milestone"]
      ; "<?xml-stylesheet href='css/milestone.css' type='text/css'?>"
      (milestone-svg)
    )))
