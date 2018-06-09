# Toxic comment classification

Allows to classify comment as being toxic, insulting, etc over a RESTful API.


Based on Kaggle "Toxic Comment Classification Challenge". Kernel for training and exporting models - https://www.kaggle.com/deniskovalenko/logistic-regression-with-words-and-char-n-grams (forked from https://www.kaggle.com/thousandvoices/logistic-regression-with-words-and-char-n-grams)

Capabilities: serve predictions in real time and provide metrics

# API
### GET /api/toxic_comments/predict?api_token=YOUR_TOKEN&parameter=my comment, definitely not toxic
Returns: 
```javascript
{"scores":{"identity_hate":0.00196645105731319,"insult":0.006155951964776735,"obscene":0.00613491609641551,"severe_toxic":0.0026068558324382532,"threat":0.0006324124967297172,"toxic":0.017618535761723918},"success":true}
```

### GET /api/toxic_comments/metrics

returns average scores, requests per minute and 2 most common labels


