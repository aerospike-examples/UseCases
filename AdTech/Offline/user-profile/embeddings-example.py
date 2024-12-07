import pandas as pd
from sqlalchemy import create_engine

# Create a connection to your SQL database
engine = create_engine('sqlite:///your_database.db')

# Load your data
df = pd.read_sql('SELECT * FROM your_table', engine)


from sklearn.preprocessing import StandardScaler, OneHotEncoder
from sklearn.compose import ColumnTransformer
from sklearn.pipeline import Pipeline

# Define your preprocessing steps
numerical_features = ['latitude', 'longitude']
categorical_features = ['ageRange', 
                        'gender', 
                        'incomeLevel', 
                        'educationLevel', 
                        'maritalStatus',
                        'employmentStatus',
                        'country',
                        'region',
                        'city',
                        'postalCode',
                        ],
# TODO: ADD INTERESTS

numerical_transformer = StandardScaler()
categorical_transformer = OneHotEncoder()

preprocessor = ColumnTransformer(
    transformers=[
        ('num', numerical_transformer, numerical_features),
        ('cat', categorical_transformer, categorical_features)
    ]
)

# Apply preprocessing
df_preprocessed = preprocessor.fit_transform(df)


from transformers import DistilBertTokenizer, DistilBertModel
import torch

# Load the tokenizer and model
tokenizer = DistilBertTokenizer.from_pretrained('distilbert-base-uncased')
model = DistilBertModel.from_pretrained('distilbert-base-uncased')

# Tokenize and embed text features
text_features = df['text_feature'].tolist()
inputs = tokenizer(text_features, return_tensors='pt', padding=True, truncation=True)
outputs = model(**inputs)

# Get the embeddings
embeddings = outputs.last_hidden_state.mean(dim=1).detach().numpy()

import numpy as np

# Combine embeddings with preprocessed tabular data
combined_data = np.hstack((df_preprocessed, embeddings))

from sklearn.linear_model import LogisticRegression
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score

# Split the data into training and testing sets
X_train, X_test, y_train, y_test = train_test_split(combined_data, df['target'], test_size=0.2, random_state=42)

# Train the model
model = LogisticRegression()
model.fit(X_train, y_train)

# Make predictions and evaluate the model
y_pred = model.predict(X_test)
accuracy = accuracy_score(y_test, y_pred)
print(f'Accuracy: {accuracy}')
