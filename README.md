## Chart Analysis [![Build Status](https://travis-ci.org/margorec/chartAnalysis.svg?branch=master)](https://travis-ci.org/margorec/chartAnalysis)
 Check the latest beta build [here at Heroku](https://cryptic-atoll-12670.herokuapp.com/chart/)

#### Description:
Its a very simple stock market analysing app. So far:
  - it downloads stock data from stooq.pl 
  - presents price time series for one company and draws a *end day* price chart
  - basing on fetched data it calculates moving averages 
  - draws long and short moving averages on chart automatically adjusted
  
##### TODOS:
- [x] Migrated to SpringBoot 2.x
- [ ] Proper error handle when non existing asset prompted


