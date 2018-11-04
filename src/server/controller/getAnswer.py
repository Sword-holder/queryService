from interface.communicate import send_query

def get_answer(all_msg):
    field_str = ('description:', 'production:', 'edition:', 'platform:')
    for i in range(len(all_msg)):
        all_msg[i] = field_str[i] + all_msg[i]
        if i == 3:
            all_msg[4] = 'diagnose'
    history_msg = '\n'.join(all_msg) if len(all_msg) > 0 else ''
    return send_query(history_msg)