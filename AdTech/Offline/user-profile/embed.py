import aerospike

from profile import UserProfile

def process_record(record): 
    # print(f"Record: {record[2]}")
    user_profile = UserProfile.from_aerospike_record(dict(record[2]))
    
    embeddings_str = user_profile.embedding_string()
    print(f"\nUserProfile id: {user_profile.id}")
    print(f"-- Embeddings: {embeddings_str}")

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