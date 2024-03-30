import json
from jinja2 import Template
import requests
import os


def make_textmessage():
    r = requests.get('http://localhost:8080/tender')
    st = r.text
    print(st)
    data = json.loads(st)
    string = ''' 
    {% for d in data -%}
        Название: {{d['name']}} - цена: {{d['price']}}
    {% endfor -%}
    '''
    tm = Template(string)
    message = tm.render(data=data)
    return message
