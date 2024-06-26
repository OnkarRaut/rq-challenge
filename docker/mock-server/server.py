from flask import Flask, request
from flask_cors import CORS
import logging
import re
import json

app = Flask(__name__)
cors = CORS(app, resources={r"*": {"origins": "*"}})

app.logger.setLevel(logging.INFO)


def get_file_data(filename):
    with open(filename) as f:
        return f.read()


STORE = {
    "GET-api/v1/employees": lambda: app.response_class(
        response=get_file_data("./data/get-all-employees-response.json"),
        status=200,
        mimetype="application/json",
    ),
    "GET-api/v1/employees/<id>": lambda: app.response_class(
        response=get_file_data("./data/get-employee-by-id-response.json"),
        status=200,
        mimetype="application/json",
    ),
    "POST-api/v1/create": lambda: app.response_class(
        response=get_file_data("./data/get-employee-by-id-response.json"),
        status=200,
        mimetype="application/json",
    ),
    "DELETE-api/v1/delete/<id>": lambda: app.response_class(
        response=json.dumps(
            {"status": "success", "message": "successfully! deleted Record"}, indent=4
        ),
        status=200,
        mimetype="application/json",
    ),
}

@app.route(f"/<path:sub_path>", methods=["GET", "POST", "DELETE"])
def handle_route(sub_path):
    masked_sub_path = re.sub(r"[0-9]+$", "<id>", sub_path)
    path_key = f"{request.method}-{masked_sub_path}"
    if path_key in STORE:
        return STORE[path_key]()

    return app.response_class(status=404)


if __name__ == "__main__":
    app.run()
