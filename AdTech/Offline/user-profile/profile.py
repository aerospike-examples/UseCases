from pydantic import BaseModel
from typing import Dict, List, Any
from enum import Enum
from datetime import datetime

class Location(BaseModel):
    country: str
    iso_code: str
    region: str
    city: str
    postal_code: str
    latitude: float
    longitude: float

    def __str__(self):
        return f"location::country:{self.country}::iso:{self.iso_code}::region:{self.region}::city:{self.city}::zip:{self.postal_code}::latitude:{self.latitude}::longitude:{self.longitude}"

class Gender(Enum):
    MALE = "male"
    FEMALE = "female"
    OTHER = "other"
    UNKNOWN = "unknown"
    NONBINARY = "nonbinary"


class Demographics(BaseModel):
    age_range: str
    gender: Gender
    income_level: str
    education_level: str
    employment_status: str
    marital_status: str
    def __str__(self):
        return f"demographics::age_range:{self.age_range}::gender:{self.gender.value}::income_level:{self.income_level}::education_level:{self.education_level}::employment_status:{self.employment_status}::marital_status:{self.marital_status}"

class EventType(Enum):
    CLICK = "click"
    VIEW = "view"
    PURCHASE = "purchase"
    OTHER = "other"

class ActivityEvent(BaseModel):
    id: str
    page_url: str
    event_type: EventType
    timestamp: datetime

    def __str__(self):
        return f"activity_event::id:{self.id}::page_url:{self.page_url}::event_type:{self.event_type.value}::timestamp:{self.timestamp}"

class Purchase(BaseModel):
    id: str
    purchase_date: datetime
    product_sku: str
    description: str
    price: float
    currency: str
    category: str
    sub_category: str
    brand: str
    model: str
    color: str
    size: str
    condition: str
    vendor_url: str
    product_url: str
    activity_event_id: str

    def __str__(self):
        return (f"purchase::id:{self.id}::purchase_date:{self.purchase_date}::product_sku:{self.product_sku}::description:{self.description}::"
                f"price:{self.price}::currency:{self.currency}::category:{self.category}::sub_category:{self.sub_category}::brand:{self.brand}::"
                f"model:{self.model}::color:{self.color}::size:{self.size}::condition:{self.condition}::vendor_url:{self.vendor_url}::"
                f"product_url:{self.product_url}::activity_event_id:{self.activity_event_id}")


class UserProfile(BaseModel):
    id: str
    created_at: datetime
    updated_at: datetime
    demographics: Demographics
    interests: List[str]
    location: Location
    activity: List[ActivityEvent]  # Assuming ActivityEvent is a dictionary
    purchases: List[Purchase]  # Assuming Purchase is a dictionary
    lineitem_ids: List[str]

    @classmethod
    def from_aerospike_record(cls, record: Dict[str, Any]):
        return cls(
            id=record['id'],
            created_at=record['createdAt'],
            updated_at=record['updatedAt'],
            demographics=Demographics(**record['demographics']),
            interests=record['interests'],
            location=Location(**record['location']),
            activity=[ActivityEvent(**event) for event in record['activity']],
            purchases=[Purchase(**purchase) for purchase in record['purchases']],
            lineitem_ids=record['lineitemIds']
        )
    def __str__(self):
        return (f"user_profile::id:{self.id}::created_at:{self.created_at}::updated_at:{self.updated_at}::demographics:{self.demographics}::"
                f"interests:{self.interests}::location:{self.location}::activity:{self.activity}::purchases:{self.purchases}::lineitem_ids:{self.lineitem_ids}")
    
    def embedding_string(self):
        return f"{self.demographics}::{self.location}::interests:{','.join(self.interests)}"