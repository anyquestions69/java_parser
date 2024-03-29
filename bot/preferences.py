import telebot
from telebot import types
from open_file import make_textmessage
from langchain.schema import HumanMessage, SystemMessage
from langchain.chat_models.gigachat import GigaChat
from giga import chat_with

auth = 'YjJmZGEwNWQtMWYxNS00NWYxLWI0OTItMDQxMDgwZDRlZGRmOjM3MmRmYWJhLTcwNDktNDEyOS05OTQ4LWIyNDY5ZTk1YWEwZg=='
token = '7185794545:AAEPgHUUcB2kkKAwadwPaF3udx-HXGgy1qE'
bot = telebot.TeleBot(token)

ai_chat = [SystemMessage(content='''Ты создан для целей продаж и маркетингового анализа рынка ведется анализ тендеров
                                          на российских электронно-торговых площадках. Твоя задача  уменьшить срок реакции
                                          на публикацию тендера и увеличить объем участия компании в таких закупках ''')]

giga = GigaChat(credentials=auth,
                model='GigaChat:latest',
                verify_ssl_certs=False)


@bot.message_handler(commands=['start'])
def hello_message(message):
    markup = types.ReplyKeyboardMarkup(resize_keyboard=True, row_width=1)
    button1 = types.KeyboardButton('Получить информацию по тендерам')
    markup.row(button1)
    button2 = types.KeyboardButton('Сайты')
    markup.row(button2)
    bot.send_message(message.chat.id, "Привет, {0.first_name}!".format(message.from_user), reply_markup=markup)
    bot.register_next_step_handler(message, typo_message)


@bot.message_handler()
def typo_message(message):
    global ai_chat
    if message.text == 'Получить информацию по тендерам':
        bot.send_message(message.chat.id, 'Список новых тендеров:')
        bot.send_message(message.chat.id, make_textmessage())
        bot.register_next_step_handler(message, typo_message)
    elif message.text == 'Сайты':
        new_markup = types.InlineKeyboardMarkup()
        new_markup.add(types.InlineKeyboardButton('Tatneft', url='https://etp.tatneft.ru/pls/tzp/f?p=220:552:::NO:::'))
        new_markup.add(types.InlineKeyboardButton('Fabrikant', url='https://www.fabrikant.ru/'))
        new_markup.add(types.InlineKeyboardButton('IceTrade', url='https://icetrade.by/'))
        bot.reply_to(message, 'Список:', reply_markup=new_markup)
        bot.register_next_step_handler(message, typo_message)
    else:
        answer = chat_with(ai_chat, message.text)
        bot.send_message(message.chat.id, answer)
        bot.register_next_step_handler(message, typo_message)


bot.infinity_polling()
