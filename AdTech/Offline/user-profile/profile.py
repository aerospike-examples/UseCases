from pydantic import BaseModel
from typing import Dict, List, Any
from enum import Enum
from datetime import datetime

class Location(BaseModel):
    country: str
    isoCode: str
    region: str
    city: str
    postalCode: str
    latitude: float
    longitude: float

    def __str__(self):
        return f"location::country:{self.country}::iso:{self.isoCode}::region:{self.region}::city:{self.city}::zip:{self.postalCode}::latitude:{self.latitude}::longitude:{self.longitude}"

class Gender(Enum):
    MALE = "MALE"
    FEMALE = "FEMALE"
    OTHER = "OTHER"
    UNKNOWN = "UNKNOWN"
    NONBINARY = "NONBINARY"


class Demographics(BaseModel):
    ageRange: str
    gender: Gender
    incomeLevel: str
    educationLevel: str
    employmentStatus: str
    maritalStatus: str
    def __str__(self):
        return f"demographics::age_range:{self.ageRange}::gender:{self.gender.value}::income_level:{self.incomeLevel}::education_level:{self.educationLevel}::employment_status:{self.employmentStatus}::marital_status:{self.maritalStatus}"

class EventType(Enum):
    CLICK = "CLICK"
    VIEW = "VIEW"
    PURCHASE = "PURCHASE"
    OTHER = "OTHER"

class ActivityEvent(BaseModel):
    id: str
    pageUrl: str
    eventType: EventType
    timestamp: datetime

    def __str__(self):
        return f"activity_event::id:{self.id}::page_url:{self.pageUrl}::event_type:{self.eventType.value}::timestamp:{self.timestamp}"

class Purchase(BaseModel):
    id: str
    purchaseDate: datetime
    productSku: str
    description: str
    price: float
    currency: str
    category: str
    subCategory: str
    brand: str
    model: str
    color: str
    size: str
    condition: str
    vendorUrl: str
    productUrl: str
    activityEventId: str

    def __str__(self):
        return (f"purchase::id:{self.id}::purchase_date:{self.purchaseDate}::productSku:{self.productSku}::description:{self.description}::"
                f"price:{self.price}::currency:{self.currency}::category:{self.category}::subCategory:{self.subCategory}::brand:{self.brand}::"
                f"model:{self.model}::color:{self.color}::size:{self.size}::condition:{self.condition}::vendorUrl:{self.vendorUrl}::"
                f"product_url:{self.productUrl}::activity_event_id:{self.activityEventId}")


class UserProfile(BaseModel):
    id: str
    createdAt: datetime
    updatedAt: datetime
    demographics: Demographics
    interests: List[str]
    location: Location
    activity: List[ActivityEvent]  # Assuming ActivityEvent is a dictionary
    purchases: List[Purchase]  # Assuming Purchase is a dictionary
    lineitemIds: List[str]

    @classmethod
    def from_aerospike_record(cls, record: Dict[str, Any]):
        demographics = Demographics(
            ageRange=record['demographics']['ageRange'],
            gender=Gender(record['demographics']['gender']),
            incomeLevel=record['demographics']['incomeLevel'],
            educationLevel=record['demographics']['educationLevel'],
            employmentStatus=record['demographics']['employmentStatus'],
            maritalStatus=record['demographics']['maritalStatus']
        )
        
        createdAt = datetime.fromtimestamp(record['createdAt']/1000)
        updatedAt = datetime.fromtimestamp(record['updatedAt']/1000) if record['updatedAt'] else None

        profile = cls(
            id=record['id'],
            createdAt=createdAt,
            updatedAt=updatedAt,
            demographics=demographics,
            interests=record['interests'],
            location=Location(**record['location']),
            activity=[ActivityEvent(**event) for event in record['activity']],
            purchases=[Purchase(**purchase) for purchase in record['purchases']],
            lineitemIds=record['lineitemIds']
        )
        return profile
    def __str__(self):
        return (f"user_profile::id:{self.id}::createdAt:{self.createdAt}::updatedAt:{self.updatedAt}::demographics:{self.demographics}::"
                f"interests:{self.interests}::location:{self.location}::activity:{self.activity}::purchases:{self.purchases}::lineitem_ids:{self.lineitem_ids}")
    
    def embedding_string(self):
        return f"user_profile_embeddings::id:{self.id}::demographics:{self.demographics}::{self.location}::interests:{','.join(self.interests)}"