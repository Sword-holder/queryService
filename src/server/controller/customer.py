class Customer(object):
    uid = 0
    messages = []
    def __init__(self, uid):
        self.uid = uid
    def add_message(self, msg):
        self.messages.append(msg)

customer_queue = []

def add_customer():
    uid = len(customer_queue)
    new_customer = Customer(uid)
    customer_queue.append(new_customer)
    return uid

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