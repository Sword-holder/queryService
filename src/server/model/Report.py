from server import db

class Repot(db.Model):
    __tablename__ = 'report'

    no = db.Column(db.Integer, primary_key=True)
    description = db.Column(db.String(4096), default='None') #问题描述
    production = db.Column(db.String(64), default='None') #产品
    platform = db.Column(db.String(64), default='None') #平台
    edition = db.Column(db.String(64), default='None') #版本
    solution_path = db.Column(db.String(4096), default='None') #解决方案存放路径

    def __init__(self, no, description, production, platform, edition, solution_path):
        self.no = no
        self.description = description
        self.production = production
        self.platform = platform
        self.edition = edition
        self.solution_path = solution_path