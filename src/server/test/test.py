from urllib import parse, request
import json
import base64
import datetime

HOST = 'localhost'
PORT = '5050'
FILE = '/queryService/'
ROOT_URL = 'http://' + HOST + ':' + PORT + FILE

def post(url, data):
    req = request.Request(url=url, data=data)
    res = request.urlopen(req)
    res = res.read()
    return res

def get(url):
    req = request.Request(url=url)
    res = request.urlopen(req)
    res = res.read()
    return res

def stringnify(data):
    data = parse.urlencode(data).encode('utf-8')
    return data


def connect():
    url = ROOT_URL + 'connect'
    res = json.loads(get(url))
    print('客服:%s' % res['ack'])
    return res['id']

def query(uid, massage):
    url = ROOT_URL + 'query'
    data = {}
    data['id'] = uid
    data['message'] = massage
    data = stringnify(data)
    res = json.loads(post(url, data))
    print('客服:%s' % res['ack'])

def close(uid):
    url = ROOT_URL + 'close'
    data = {}
    data['id'] = uid
    data = stringnify(data)
    res = json.loads(post(url, data))
    print('客服:%s' % res['ack'])


'''测试代码'''
def test_query():
    uid = connect()
    while True:
        message = input('我:')
        if message == '再见':
            break
        query(uid, message)
    close(uid)

if __name__ == '__main__':
    test_query()