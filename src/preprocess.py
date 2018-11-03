#coding=utf-8

import os

TXT_NUMBER = 3411
source_path = './已有故障分析报告/'
answer_path = './server/解决方案/'

def txt_rename():
    count = 0
    for file in os.listdir(source_path):
        os.rename(os.path.join(source_path,file), os.path.join(source_path,str(count)+".txt"))
        count += 1


def create_answer():
    for i in range(TXT_NUMBER):
        open(answer_path + str(i) + '.txt', 'w')


def cat_answer():
    absence_num = 0

    for i in range(TXT_NUMBER):
        context = open(source_path + str(i) + '.txt', 'r').read().split('\n')
        ans = ''
        back = ''
        for col in range(len(context)): #遍历文本中的每一行
            if (context[col].startswith('解答:') or context[col].startswith('解答：')):
                ans = '\n'.join(context[col + 1:])
                back = '\n'.join(context[:col])
                break

        if ans != '':
            open(source_path + str(i) + '.txt', 'w').write(back)
            open(answer_path + str(i) + '.txt', 'w').write(ans)
        elif open(answer_path + str(i) + '.txt', 'r').read() == '':
            open('./缺失/' + str(i) + '.txt', 'w').write('\n'.join(context))
            absence_num = absence_num + 1

    return absence_num
          

def get_info():
    for i in range(10):
        context = open(source_path + str(i) + '.txt', 'r').read().split('\n')
        for line in context: #遍历文本中的每一行
            name = '版本'
            if (line.startswith(name + ':') or line.startswith(name + '：')):
                open('./server/' + name + '/' + str(i) + '.txt', 'w').write(line[3:])
            name = '产品'
            if (line.startswith(name + ':') or line.startswith(name + '：')):
                open('./server/' + name + '/' + str(i) + '.txt', 'w').write(line[3:])
            name = '平台'
            if (line.startswith(name + ':') or line.startswith(name + '：')):
                open('./server/' + name + '/' + str(i) + '.txt', 'w').write(line[3:])
            name = '软件产品'
            if (line.startswith(name + ':') or line.startswith(name + '：')):
                open('./server/产品/' + str(i) + '.txt', 'w').write(line[5:])
            name = '软件版本'
            if (line.startswith(name + ':') or line.startswith(name + '：')):
                open('./server/版本/' + str(i) + '.txt', 'w').write(line[5:])


if __name__ == '__main__':    
    get_info()