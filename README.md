# Toxic comment classification

Allows to classify comment as being toxic, insulting, etc over a RESTful API.


Based on Kaggle "Toxic Comment Classification Challenge". Kernel for training and exporting models - https://www.kaggle.com/deniskovalenko/logistic-regression-with-words-and-char-n-grams (forked from https://www.kaggle.com/thousandvoices/logistic-regression-with-words-and-char-n-grams)

Capabilities: serve predictions in real time and provide metrics

# API
### GET /api/toxic_comments/predict?api_token=YOUR_TOKEN&parameter=my comment, definitely not toxic
api_token should be set as environment variable for Scala app. 


Result: 
```javascript
{"scores":{"identity_hate":0.00196645105731319,"insult":0.006155951964776735,"obscene":0.00613491609641551,"severe_toxic":0.0026068558324382532,"threat":0.0006324124967297172,"toxic":0.017618535761723918},"success":true}
```

### GET /api/toxic_comments/metrics
Result:
```javascript
{"metrics":{"requestsPerMinute":4,"mostCommonLabels":["toxic","obscene"],"toxicMean":0.276513919586555,"severe_toxicMean":0.24385146656244983,"obsceneMean":0.25744020103790544,"threatMean":0.004259511579034224,"insultMean":0.25579802629887466,"identity_hateMean":0.017571348458626793}}
```
returns average scores, requests per minute and 2 most common labels

# Building:
Projects needs 3 containers: 

Kafka container (spotify/kafka), 
predictor:1.0,
model-service:1.0

cd predictor
sbt docker:publishLocal

cd ../model-service
docker build -t model-service:1.0 .
cd ..

## Start
docker-compose up

(main app starts on 9005 port)

# Discussion:

### Pros:
* Scalability: By separating web service from model service we can scale model throughtput by just deploying more containers, as long as they belong to same Kafka consumer group.
* Extendibility: Prediction pipeline is model-agnostic - it just passes request to matching kafka topic and displays whatever was send by model
* By passing scores through Kafka we can use Kafka Streaming (or other frameworks) to compute statistics about them
* Metrics are pre-computed, so additional requests from clients won't increase load on Kafka
* Raw scores from each model are displayed (instead of normalizing so they would add up to 1) - for "neutral" comments we want to expect all scores being low, but with normalization likely one of scores would dominate (say, neutral comment has 0.6 score of being threat)


### Cons:
* Feature extraction takes more than 1 second, which makes it quite expensive operation. A possible improvement would be to batch queries together and perform transform on a batch.
* No caching yet
* We might need to put load balancer before Scala app to scale it up. 
* For that it's needed to get rid of my bicycle-style stream processing and use Kafka streams, as for now state about recent request is stored in-memory.
* Currently deployment is just docker-compose. Some container orchestration tool might be useful.
* Kafka 'cluster' currently persists inside container which isn't great, need to attach external volume
* I haven't managed to calculate metrics with Kafka Streaming, so current metrics calculations are quite naive








