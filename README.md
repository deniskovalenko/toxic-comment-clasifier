# Toxic comment classification

Allows to classify comment as being toxic, insulting, etc over a RESTful API.


Based on Kaggle "Toxic Comment Classification Challenge". Kernel for training and exporting models - https://www.kaggle.com/deniskovalenko/logistic-regression-with-words-and-char-n-grams (forked from https://www.kaggle.com/thousandvoices/logistic-regression-with-words-and-char-n-grams)

Capabilities: serve predictions in real time and provide metrics

# API
### GET /api/toxic_comments/predict?api_token=YOUR_TOKEN&parameter=my comment, definitely not toxic
api_token should be set as environment variable for Scala app. 


Returns: 
```javascript
{"scores":{"identity_hate":0.00196645105731319,"insult":0.006155951964776735,"obscene":0.00613491609641551,"severe_toxic":0.0026068558324382532,"threat":0.0006324124967297172,"toxic":0.017618535761723918},"success":true}
```

### GET /api/toxic_comments/metrics
```javascript
{"metrics":{"requestsPerMinute":4,"mostCommonLabels":["toxic","obscene"],"toxicMean":0.276513919586555,"severe_toxicMean":0.24385146656244983,"obsceneMean":0.25744020103790544,"threatMean":0.004259511579034224,"insultMean":0.25579802629887466,"identity_hateMean":0.017571348458626793}}
```
returns average scores, requests per minute and 2 most common labels

# Start:



