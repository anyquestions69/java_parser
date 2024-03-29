FROM python:3.10.4

WORKDIR /bot 

RUN python -m pip install --upgrade pip

COPY . /bot

RUN pip3 install -r ./requirements.txt --verbose

CMD [ "python3", "/bot/preferences.py" ]