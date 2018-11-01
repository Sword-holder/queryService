import socket

def send_query():
    print('准备开始连接。。。')
    client = socket.socket()#声明socket类型，同时生成socke连接t对象
    client.connect(('localhost', 55533))  #连接到localhost主机的55522端口
    print('连接已建立。。。')

    query = "description:宕机\nproduction:IBM Notes\nedition:9.0\nplatform:Mac OS"
    client.send(query.encode('utf-8'))
    print('已发送查询字串。。。')
    response = client.recv(2048)
    print("从服务器接收到的数据为：", response.decode())
    client.close()

if __name__ == '__main__':
    send_query()