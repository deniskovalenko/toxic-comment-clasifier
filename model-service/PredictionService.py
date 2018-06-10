from sklearn.externals import joblib


class PredictionService(object):
    def __init__(self):
        self.models = {}
        self.vectorizer = joblib.load('models/vectorizer.pkl')
        self.models['toxic'] = joblib.load('models/toxic_lr.pkl')
        self.models['severe_toxic'] = joblib.load('models/severe_toxic_lr.pkl')
        self.models['obscene'] = joblib.load('models/obscene_lr.pkl')
        self.models['threat'] = joblib.load('models/threat_lr.pkl')
        self.models['insult'] = joblib.load('models/insult_lr.pkl')
        self.models['identity_hate'] = joblib.load('models/identity_hate_lr.pkl')
        self.class_names = ['toxic', 'severe_toxic', 'obscene', 'threat', 'insult', 'identity_hate']

    def predict(self, project, parameter):
        scores = {}
        score_values = []
        response = {}

        features = self.vectorizer.transform([parameter])

        for class_name in self.class_names:
            positive_probability = self.models[class_name].predict_proba(features)[0, 1]
            scores[class_name] = positive_probability
            score_values.append(positive_probability)

        response['scores'] = scores
        return response


