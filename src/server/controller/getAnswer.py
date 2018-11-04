from interface.communicate import send_query


def get_answer(all_msg):
    field_str = ('description:', 'production:', 'edition:', 'platform:')
    for i in range(len(all_msg)):
        if not all_msg[i].startswith(field_str[i]):
            all_msg[i] = field_str[i] + all_msg[i]
        if i == 3:
            all_msg.append('diagnose')
    history_msg = '\n'.join(all_msg) if len(all_msg) > 0 else ''
    return send_query(history_msg)