flow_id = 0

class Customer(object):
    uid = 0
    question_no = 0
    messages = []
    def __init__(self, uid):
        self.uid = uid
        self.messages = []
    def add_message(self, msg):
        self.messages.append(msg)

customer_queue = []

def add_customer():
    global flow_id
    new_customer = Customer(flow_id)
    flow_id = flow_id + 1
    customer_queue.append(new_customer)
    return new_customer.uid

def rm_customer(uid):
    for customer in customer_queue:
        if customer.uid == uid:
            customer_queue.remove(customer)
            break

def add_new_query(uid, message): #将新的信息放入到历史消息，并返回所有历史消息
    for customer in customer_queue:
        if customer.uid == uid:
            customer.add_message(message)
            return customer.messages
    return None

def find_question_no(uid):
    for customer in customer_queue:
        if customer.uid == uid:
            customer.question_no = customer.question_no + 1
            return customer.question_no - 1

def customer_num():
    return len(customer_queue)

def show_customer():
    print('==========================')
    for customer in customer_queue:
        print(customer.uid)
        print(customer.messages)
    print('==========================')