/* initial records added for testing purpose*/
INSERT INTO obstetrics (MID, initDate, LMP, EDD, weekPregnant, pregnancyStatus)
values(1, '2018-11-14', '2018-10-1', '2019-7-8', '6-2', 'Initial');

INSERT INTO obstetrics (MID, initDate, LMP, EDD, weekPregnant, pregnancyStatus)
values(2, '2018-11-02', '2018-8-7', '2019-5-14', '12-3', 'Initial');

/* prior pregnancy records added for testing purpose*/
INSERT INTO obstetrics (MID, concepYear, weekPregnant, hrsLabor, weightGain, deliveryType, pregnancyStatus, multiPregnancy, babyCount)
values(1, 2014, '40-3', 20, 30.5, 'Vaginal Delivery Vacuum Assist', 'Complete', true, 2);

INSERT INTO obstetrics (MID, concepYear, weekPregnant, hrsLabor, weightGain, deliveryType, pregnancyStatus, multiPregnancy, babyCount)
values(2, 2015, '12-3', 3, 11.3, 'Miscarriage', 'Complete', false, 1);


/* office visit obstetrics record added for testing purpose*/
INSERT INTO obstetrics (MID, initDate, LMP, EDD, weekPregnant, weight, bloodPressureL, bloodPressureH, FHR, pregnancyStatus, multiPregnancy, babyCount, lyingPlacenta)
values(1, '2018-12-11', '2018-10-1', '2019-7-8', '10-1', 135.5, 70, 120, 76, 'OfficeVisit', false, 1, false);

INSERT INTO obstetrics (MID, initDate, LMP, EDD, weekPregnant, weight, bloodPressureL, bloodPressureH, FHR, pregnancyStatus, multiPregnancy, babyCount, lyingPlacenta)
values(2, '2018-12-02', '2018-8-7', '2019-5-14', '16-5', 143.5, 68, 118, 74, 'OfficeVisit', true, 2, false);

