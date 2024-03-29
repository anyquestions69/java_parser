from langchain.schema import HumanMessage, SystemMessage
from langchain.chat_models.gigachat import GigaChat

auth = 'YjJmZGEwNWQtMWYxNS00NWYxLWI0OTItMDQxMDgwZDRlZGRmOjM3MmRmYWJhLTcwNDktNDEyOS05OTQ4LWIyNDY5ZTk1YWEwZg=='

giga = GigaChat(credentials=auth,
                model='GigaChat:latest',
                verify_ssl_certs=False)


def chat_with(history, message):
    history.append(HumanMessage(content=message))
    answer = giga(history)
    history.append(answer)
    return answer.content
