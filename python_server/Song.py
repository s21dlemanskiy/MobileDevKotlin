
from dataclasses import dataclass

@dataclass
class Song:
    id: int
    title: str
    text: str
    author_id: int
