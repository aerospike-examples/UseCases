import aerospike
from aerospike_vector_search import types, client, admin
from transformers import AutoTokenizer, AutoModel
from transformers import DistilBertTokenizer, DistilBertModel
import torch
from UserProfile import UserProfile

# Load the pre-trained DistilBERT model
tokenizer = DistilBertTokenizer.from_pretrained('distilbert-base-uncased')
model = DistilBertModel.from_pretrained('distilbert-base-uncased')

# NAMESPACE is the namespace that the indexed data will be stored in
NAMESPACE = "rtb"
# SET_NAME is the set that the indexed data will be stored in
SET = "profiles"
# INDEX_NAME is the name of the HNSW index to create
INDEX_NAME = "profile_index"
# VECTOR_FIELD is the Aerospike record bin that stores its vector data
# The created index will use the data in this bin to perform nearest neighbor searches etc
VECTOR_FIELD = "vector"
# Dimensions produced by the embedding model
MODEL_DIM = 768


# Initialize aerospike client
aerospike_client = aerospike.client({"hosts": [("localhost", 3000)]})
# Initialize vector admin client
vector_seed = types.HostPort(host="localhost", port=5500)
vector_admin = admin.Client(seeds=vector_seed)
vector_client = client.Client(seeds=vector_seed)


def create_index():
    try:
        index_list = vector_admin.index_list()
        if INDEX_NAME in index_list:
            print("index already exists")
            return
        print(f"creating index: {INDEX_NAME}")
        vector_admin.index_create(
            namespace=NAMESPACE,
            name=INDEX_NAME,
            vector_field=VECTOR_FIELD,
            dimensions=MODEL_DIM,
        )
        print("index created")
    except Exception as e:
        print("failed creating index " + str(e))
        pass


def create_profile_embedding(value):
    text = str(value)
    inputs = tokenizer(text, return_tensors='pt', truncation=True, padding=True)
    with torch.no_grad():
        outputs = model(**inputs)
    return outputs.last_hidden_state.mean(dim=1).squeeze().numpy()
    


def process_record(record): 
    # print(f"Record: {record[2]}")
    user_profile = UserProfile.from_aerospike_record(dict(record[2]))

    # print(user_profile)
   
    vector = create_profile_embedding(user_profile)
    

    vector_client.upsert(
        namespace=NAMESPACE,
        set_name=SET,
        key=user_profile.id,
        record_data={
            "vector": vector,
        },
    )
    print(f"\nUserProfile indexed: {user_profile.id}")


def scan_user_profiles():

    try:
        scan = aerospike_client.scan(NAMESPACE, SET)
        scan.foreach(process_record)
    except Exception as e: 
        print("Error: ", e)
    finally:
        vector_client.close()
        vector_admin.close()
        aerospike_client.close()
    return

if __name__ == '__main__':
    create_index()
    # scan_user_profiles()
    print("Done")