FROM python:3.6.5
COPY ./app /app
WORKDIR /app
RUN pip install -r requirements.txt
ENTRYPOINT ["python"]
# Run the start script, it will check for an /app/prestart.sh script (e.g. for migrations)
# And then will start Supervisor, which in turn will start Nginx and uWSGI
CMD ["App.py"]