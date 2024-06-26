FROM python:3.11

WORKDIR /app
COPY . .
RUN pip3 install -r requirements.txt

ENV FLASK_APP=server.py

CMD ["flask", "run", "--host", "0.0.0.0"]

EXPOSE 5000
