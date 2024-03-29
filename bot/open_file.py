import json
from jinja2 import Template


def make_textmessage(file_name):
    with open(file=file_name, encoding='utf-8') as json_file:
        data = json.load(json_file)
    string = ''' 
    {% for d in data -%}
        Название: {{d['name']}} - цена: {{d['price']}}
    {% endfor -%}
    '''
    tm = Template(string)
    message = tm.render(data=data)
    return message
