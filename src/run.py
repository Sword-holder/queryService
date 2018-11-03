# 本服务器程序在类unix平台上运行，使用flask作为框架，使用mysql作为数据库

from server import app

if __name__ == '__main__':
    app.run(host='0.0.0.0', port='5050', debug=True)