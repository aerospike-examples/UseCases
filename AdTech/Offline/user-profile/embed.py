import aerospike

from profile import UserProfile

def process_record(record): 
    dict_record = dict(record[2])
    id = dict_record['id']
    demographics = dict_record['demographics']
    interests = dict_record['interests']
    location = dict_record['location']
    user_profile = UserProfile.from_aerospike_record(dict(record[2]))
    print("UserProfile:", user_profile)

def scan_user_profiles():
    config = {
        'hosts': [('127.0.0.1', 3000)]
    }

    client = aerospike.client(config).connect()
    try:
        scan = client.scan('rtb', 'profiles')
        scan.foreach(process_record)
    except Exception as e: 
        print("Error: ", e)
    client.close()

if __name__ == '__main__':
    scan_user_profiles()