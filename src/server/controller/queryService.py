from server.model import Report
from server import app, db
from flask import Flask, jsonify, request
from server.controller.getAnswer import get_answer
from server.controller.customer import add_customer, rm_customer, add_new_query, find_question_no, customer_num

WELCOME_STR = '您好，请问有什么需要帮助的吗？'
QUERY_STR = (
    '请问您的产品是什么？',
    '请问您的产品版本号是什么？',
    '请问您的运行平台是什么？'
)
EXIT_STR = '很高兴为您服务，再见！'
SORRY_STR = '很抱歉，没有找到合适的答案'

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
    if response['ack'] == 'next':
        response['ack'] = QUERY_STR[find_question_no(uid)]
    response['disc'] = False
    if response['ack'].endswith('disc'): #主动断开连接
        response['disc'] = True
        response['ack'] = response['ack'][0:-4]
        if response['ack'] == 'sorry':
            response['ack'] = SORRY_STR
    return jsonify(response)


@app.route('/queryService/close', methods=['POST'])
def close():
    response = {}
    uid = int(request.form.get('id')) #类型应该保证为int型
    rm_customer(uid)
    response['code'] = 0
    response['ack'] = EXIT_STR
    print(customer_num())
    return jsonify(response)