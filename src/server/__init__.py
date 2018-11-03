from flask import Flask
from flask_sqlalchemy import SQLAlchemy

app = Flask(__name__)


# 加载配置文件
app.config.from_object('server.config')     #模块下的config文件名，不用加py后缀

#创建数据库对象
db = SQLAlchemy(app)

from server.model import Report
from server.controller import queryService