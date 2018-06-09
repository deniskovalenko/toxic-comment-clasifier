from flask import Flask, abort
from flask import request
from flask import jsonify
from flask_login import LoginManager
from flask_cors import CORS
import sys
from flask_caching import Cache
from PredictionService import PredictionService

app = Flask(__name__)
prediction_service = PredictionService()
CORS(app)
login_manager = LoginManager()
login_manager.init_app(app)
cache = Cache(app,config={'CACHE_TYPE': 'simple'})

@cache.cached(timeout=1000)
@app.route('/api/<project>/predict')
def predict(project):
    #project is a namespace for ML task. "toxic_comments" in our case. Used to retrieve matching model/etc

    api_token = request.args.get('api_token')
    # checking for correct API token
    if api_token != app.config['api_token']:
        return abort(401)

    #content of request. In toxic comment use-case - comment, in other possible use cases - deal ID
    parameter = request.args.get('parameter')
    if parameter is None:
        return jsonify(success= False, data={'parameter required' : True}, message = 'supply data as "parameter" in GET parameters')

    result = predict(project, parameter)
    #todo store scores. (Or use Kafka both for connecting with model and
    return jsonify(result)

@cache.cached(timeout=3, key_prefix='all_predictions')
def predict(project, parameter):
    print('no cache')
    result = prediction_service.predict(project, parameter)
    result['success'] = True
    return result

@app.route('/api/<project>/metrics')
def deals(project):
    #todo get date/time/minute. If cache is available for the minute use it
    #if cache[project][matric][time]:
    # return from cache

    #todo calculate metrics (Streaming? what's more frequent - predictions or metric queries? prediction - write, metric - read. Optimize for what?


    return jsonify(metric = 'metric metric metric')

@app.errorhandler(401)
def unauthorized_handler(e):
    return jsonify(success= False, data={'api_token required' : True}, message = 'Supply api_token as a request parameter')

if __name__ == '__main__':
    app.config['api_token'] = sys.argv[1]
    #todo read model files directory, etc
    #for example, list of allowed "projects" - toxic:iris_prediction:...
    app.run(host='0.0.0.0')
