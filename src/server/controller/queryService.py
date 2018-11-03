from server.model import Report
from server import app, db
from flask import Flask, jsonify, request
from server.controller.getAnswer import get_answer
from server.controller.customer import add_customer, rm_customer, add_new_query

WELCOME_STR = '您好，请问有什么需要帮助的吗？'
EXIT_STR = '再见！'

@app.route('/queryService/connect', methods=['GET'])
def connect():
    response = {}
    response['code'] = 0
    response['id'] = add_customer()
    response['ack'] = WELCOME_STR
    return jsonify(response)
    

@app.route('/queryService/query', methods=['POST'])
def query():
    response = {}
    uid = int(request.form.get('id')) #类型应该保证为int型
    message = request.form.get('message')
    all_msg = add_new_query(uid, message)
    response['ack'] = get_answer(all_msg)
    response['code'] = 0
    return jsonify(response)


@app.route('/queryService/close', methods=['POST'])
def close():
    response = {}
    uid = int(request.form.get('id')) #类型应该保证为int型
    rm_customer(uid)
    response['code'] = 0
    response['ack'] = EXIT_STR
    return jsonify(response)