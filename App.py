from flask import Flask
from flask import request
from flask import jsonify
from flask import security
from flask_cors import CORS
import sys


app = Flask(__name__)
#allowing cross-origin requests
CORS(app)

@app.route('/api/<project>/predict')
def predict(project):
    #project is namespace for ML task. "toxic_comments" in our case. Used to retrieve matching model/etc

    api_token = request.args.get('api_token')
    # checking for correct API token
    if api_token != app.config['api_token']:
        return abort(401)

    #content of request. In toxic comment use-case - comment, in other possible use cases - deal ID
    #todo check for non-empty, or any other possible validation
    parameter = request.args.get('parameter')

    #todo cache - use cache
    #cache[project][prediction][parameter]

    #todo call ML model

    #todo store scores. (Or use Kafka both for connecting with model and


    scores = {}
    scores["toxic"] = 1.0
    scores["very-toxic"] = 0.0
    response = {}
    response["result"] = scores
    return jsonify(response)

@app.route('/api/<project>/metrics')
def deals(project):
    #todo get date/time/minute. If cache is available for the minute use it
    #if cache[project][matric][time]:
    # return from cache

    #todo calculate metrics (Streaming? what's more frequent - predictions or metric queries? prediction - write, metric - read. Optimize for what?


    return jsonify(metric)

@app.login_manager.unauthorized_handler
def unauthorized_handler():
    return jsonify(success= False, data={'api_token required' : True}, message = 'Supply api_token as a request parameter')

if __name__ == '__main__':
    app.config['api_token'] = sys.argv[1]
    #todo read model files directory, etc
    app.run(host='0.0.0.0')
